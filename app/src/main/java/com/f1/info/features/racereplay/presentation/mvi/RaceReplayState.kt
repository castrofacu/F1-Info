package com.f1.info.features.racereplay.presentation.mvi

import com.f1.info.features.racereplay.presentation.model.DriverPosition

data class RaceReplayState(
    val isLoading: Boolean = true,
    val isPlaying: Boolean = false,
    val currentRaceTime: String = "",
    val drivers: List<DriverPosition> = emptyList(),
    val error: String? = null
)
