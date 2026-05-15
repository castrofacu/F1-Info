package com.f1.info.presentation.racereplay.mvi

sealed interface RaceReplayIntent {
    data object LoadRaceData : RaceReplayIntent
    data object PlayStop : RaceReplayIntent
    data object RetryLoad : RaceReplayIntent
}
