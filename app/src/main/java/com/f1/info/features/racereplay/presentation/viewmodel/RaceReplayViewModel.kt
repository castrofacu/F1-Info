package com.f1.info.features.racereplay.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.f1.info.core.common.AppConstants
import com.f1.info.core.domain.model.DomainError
import com.f1.info.core.domain.model.fold
import com.f1.info.core.domain.usecase.GetDriversUseCase
import com.f1.info.core.domain.usecase.GetPositionsUseCase
import com.f1.info.core.presentation.mvi.BaseViewModel
import com.f1.info.core.presentation.util.ErrorMessageMapper
import com.f1.info.features.racereplay.presentation.model.DriverPosition
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayEffect
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayIntent
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayState
import com.f1.info.features.racereplay.presentation.processor.RaceTimelineProcessor
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.TreeMap

class RaceReplayViewModel(
    private val getPositionsUseCase: GetPositionsUseCase,
    private val getDriversUseCase: GetDriversUseCase,
    private val timelineProcessor: RaceTimelineProcessor
) : BaseViewModel<RaceReplayState, RaceReplayIntent, RaceReplayEffect>(RaceReplayState()) {

    private var replayJob: Job? = null
    private val isPlaying = MutableStateFlow(false)

    private var timelineSnapshots: TreeMap<Instant, List<DriverPosition>> = TreeMap()

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
                            timelineSnapshots =
                                timelineProcessor.buildTimeline(allPositions, drivers)
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
            val startTime = timelineSnapshots.firstKey() ?: return@launch

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
        return timelineSnapshots.floorEntry(currentTime)?.value ?: emptyList()
    }

    private fun createRaceTimeFlow(startTime: Instant): Flow<Instant> = flow {
        var currentTime = startTime
        val endTime = timelineSnapshots.keys.maxOrNull() ?: return@flow
        val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
        while (currentTime <= endTime) {
            emit(currentTime)
            updateState { copy(currentRaceTime = hourFormatter.format(currentTime)) }
            delay(REPLAY_TICK_DELAY_MS)
            currentTime = currentTime.plus(REPLAY_TIME_ADVANCE_MINUTES, ChronoUnit.MINUTES)
        }
    }
}
