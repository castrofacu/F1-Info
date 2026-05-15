package com.f1.info.presentation.racereplay.mvi

import com.f1.info.domain.model.DriverPosition

data class RaceReplayState(
    val isLoading: Boolean = true,
    val isPlaying: Boolean = false,
    val currentRaceTime: String = "",
    val drivers: List<DriverPosition> = emptyList(),
    val error: String? = null
)
