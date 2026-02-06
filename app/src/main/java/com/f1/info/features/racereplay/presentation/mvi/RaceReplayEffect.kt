package com.f1.info.features.racereplay.presentation.mvi

sealed interface RaceReplayEffect {
    data class ShowError(val message: String) : RaceReplayEffect
}
