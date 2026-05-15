package com.f1.info.presentation.racereplay.mvi

sealed interface RaceReplayEffect {
    data class ShowError(val message: String) : RaceReplayEffect
}
