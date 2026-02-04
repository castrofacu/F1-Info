package com.f1.info.features.racereplay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1.info.core.domain.model.Driver
import com.f1.info.core.domain.model.Position
import com.f1.info.core.domain.usecase.GetDriversUseCase
import com.f1.info.core.domain.usecase.GetPositionsUseCase
import com.f1.info.features.racereplay.presentation.model.DriverPosition
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayEffect
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayIntent
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class RaceReplayViewModel(
    private val getPositionsUseCase: GetPositionsUseCase,
    private val getDriversUseCase: GetDriversUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RaceReplayState())
    val state = _state.asStateFlow()

    private val _effect = Channel<RaceReplayEffect>()
    val effect = _effect.receiveAsFlow()

    private var replayJob: Job? = null
    private val isPlaying = MutableStateFlow(false)

    private var timelineSnapshots: Map<Instant, List<DriverPosition>> = emptyMap()
    private var drivers: List<Driver> = emptyList()

    companion object {
        private const val LAST_2025_RACE_SESSION_KEY = 9839
        private const val REPLAY_TICK_DELAY_MS = 500L
        private const val REPLAY_TIME_ADVANCE_MINUTES = 2L
    }

    fun handleIntent(intent: RaceReplayIntent) {
        when (intent) {
            is RaceReplayIntent.LoadRaceData -> loadRaceData()
            is RaceReplayIntent.PlayPause -> togglePlayPause()
            is RaceReplayIntent.RetryLoad -> loadRaceData()
        }
    }

    private fun loadRaceData() {
        viewModelScope.launch {
            _state.value = RaceReplayState(isLoading = true)

            val positionsResult = getPositionsUseCase(LAST_2025_RACE_SESSION_KEY)
            val driversResult = getDriversUseCase(LAST_2025_RACE_SESSION_KEY)

            if (positionsResult.isSuccess && driversResult.isSuccess) {
                val allPositions = positionsResult.getOrThrow()
                drivers = driversResult.getOrThrow()
                timelineSnapshots = buildTimelineSnapshots(allPositions, drivers)
                
                startReplay()
            } else {
                val errorMessage = "Failed to load race data"
                _state.value = RaceReplayState(isLoading = false, error = errorMessage)
                _effect.send(RaceReplayEffect.ShowError(errorMessage))
            }
        }
    }

    private fun buildTimelineSnapshots(
        positions: List<Position>,
        drivers: List<Driver>
    ): Map<Instant, List<DriverPosition>> {
        val positionsByDriver = positions
            .groupBy { it.driverNumber }
            .mapValues { (_, driverPositions) -> driverPositions.sortedBy { it.date } }

        val instants = positions.map { it.date }.distinct().sorted()

        return instants.associateWith { timestamp ->
            drivers.map { driver ->
                val positionData = positionsByDriver[driver.number]
                    ?.lastOrNull { it.date <= timestamp }

                DriverPosition(
                    number = driver.number,
                    name = driver.fullName,
                    teamName = driver.teamName,
                    headshotUrl = driver.headshotUrl,
                    teamColour = driver.teamColour,
                    position = positionData?.position ?: 0
                )
            }.sortedBy { it.position }
        }
    }

    private fun togglePlayPause() {
        isPlaying.value = !isPlaying.value
        _state.value = _state.value.copy(isPlaying = isPlaying.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startReplay() {
        replayJob?.cancel()
        replayJob = viewModelScope.launch {
            val startTime = timelineSnapshots.keys.minOrNull() ?: return@launch
            
            // Set initial state immediately
            val initialSnapshot = timelineSnapshots[startTime] ?: emptyList()
            _state.value = _state.value.copy(
                isLoading = false,
                drivers = initialSnapshot
            )

            isPlaying.flatMapLatest { playing ->
                if (playing) createRaceTimeFlow(startTime) else flow { }
            }
                .onEach { currentTime ->
                    val snapshot = timelineSnapshots.entries
                        .filter { it.key <= currentTime }
                        .maxByOrNull { it.key }
                        ?.value ?: emptyList()

                    _state.value = _state.value.copy(drivers = snapshot)
                }
                .collect()
        }
    }

    private fun createRaceTimeFlow(startTime: Instant): Flow<Instant> = flow {
        var currentTime = startTime
        val endTime = timelineSnapshots.keys.maxOrNull() ?: return@flow
        val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
        while (currentTime <= endTime) {
            emit(currentTime)
            _state.value = _state.value.copy(
                currentRaceTime = hourFormatter.format(currentTime)
            )
            delay(REPLAY_TICK_DELAY_MS)
            currentTime = currentTime.plus(REPLAY_TIME_ADVANCE_MINUTES, ChronoUnit.MINUTES)
        }
    }
}
