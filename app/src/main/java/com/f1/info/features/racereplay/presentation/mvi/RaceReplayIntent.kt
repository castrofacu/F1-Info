package com.f1.info.features.racereplay.presentation.mvi

sealed interface RaceReplayIntent {
    data object LoadRaceData : RaceReplayIntent
    data object PlayStop : RaceReplayIntent
    data object RetryLoad : RaceReplayIntent
}
