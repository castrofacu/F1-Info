package com.f1.info.features.racereplay.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.f1.info.core.common.AppConstants
import com.f1.info.core.presentation.mvi.BaseViewModel
import com.f1.info.core.presentation.util.ErrorMessageMapper
import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.fold
import com.f1.info.domain.usecase.BuildRaceTimelineUseCase
import com.f1.info.domain.usecase.GetDriversUseCase
import com.f1.info.domain.usecase.GetPositionsUseCase
import com.f1.info.domain.model.DriverPosition
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayEffect
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayIntent
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes

class RaceReplayViewModel(
    private val getPositionsUseCase: GetPositionsUseCase,
    private val getDriversUseCase: GetDriversUseCase,
    private val buildRaceTimeline: BuildRaceTimelineUseCase
) : BaseViewModel<RaceReplayState, RaceReplayIntent, RaceReplayEffect>(RaceReplayState()) {

    private var replayJob: Job? = null
    private val isPlaying = MutableStateFlow(false)

    private var timelineSnapshots: Map<Instant, List<DriverPosition>> = emptyMap()

    companion object {
        private const val REPLAY_TICK_DELAY_MS = 500L
        private const val REPLAY_TIME_ADVANCE_MINUTES = 2L
    }

    override fun handleIntent(intent: RaceReplayIntent) {
        when (intent) {
            is RaceReplayIntent.LoadRaceData -> loadRaceData()
            is RaceReplayIntent.PlayStop -> togglePlayStop()
            is RaceReplayIntent.RetryLoad -> loadRaceData()
        }
    }

    private fun loadRaceData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            val positionsResultDeferred = async { getPositionsUseCase(AppConstants.LAST_2025_RACE_SESSION_KEY) }
            val driversResultDeferred = async { getDriversUseCase(AppConstants.LAST_2025_RACE_SESSION_KEY) }

            val positionsResult = positionsResultDeferred.await()
            val driversResult = driversResultDeferred.await()

            positionsResult.fold(
                onSuccess = { allPositions ->
                    driversResult.fold(
                        onSuccess = { drivers ->
                            timelineSnapshots = buildRaceTimeline(allPositions, drivers)
                            startReplay()
                        },
                        onFailure = { handleError(it) }
                    )
                },
                onFailure = { handleError(it) }
            )
        }
    }

    private fun handleError(error: DomainError) {
        val errorMessage = ErrorMessageMapper.map(error)
        updateState { copy(isLoading = false, error = errorMessage) }
        viewModelScope.launch { sendEffect(RaceReplayEffect.ShowError(errorMessage)) }
    }

    private fun togglePlayStop() {
        isPlaying.update { !it }
        updateState { copy(isPlaying = this@RaceReplayViewModel.isPlaying.value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startReplay() {
        replayJob?.cancel()
        replayJob = viewModelScope.launch {
            val startTime = timelineSnapshots.keys.firstOrNull() ?: return@launch

            val initialSnapshot = getSnapshotAtTime(startTime)
            updateState { copy(isLoading = false, drivers = initialSnapshot) }

            isPlaying.flatMapLatest { playing ->
                if (playing) createRaceTimeFlow(startTime) else flow { }
            }
                .onEach { currentTime ->
                    val snapshot = getSnapshotAtTime(currentTime)
                    updateState { copy(drivers = snapshot) }
                }
                .collect()
        }
    }

    private fun getSnapshotAtTime(currentTime: Instant): List<DriverPosition> {
        return timelineSnapshots.entries
            .lastOrNull { it.key <= currentTime }
            ?.value ?: emptyList()
    }

    private fun createRaceTimeFlow(startTime: Instant): Flow<Instant> = flow {
        var currentTime = startTime
        val endTime = timelineSnapshots.keys.maxOrNull() ?: return@flow

        while (currentTime <= endTime) {
            emit(currentTime)
            val localTime = currentTime.toLocalDateTime(TimeZone.currentSystemDefault())
            val formatted = "%02d:%02d".format(localTime.hour, localTime.minute)
            updateState { copy(currentRaceTime = formatted) }
            delay(REPLAY_TICK_DELAY_MS)
            currentTime += REPLAY_TIME_ADVANCE_MINUTES.minutes
        }
    }
}
