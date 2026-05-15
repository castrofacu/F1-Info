package com.f1.info.features.racereplay.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.info.core.navigation.AppDestination
import com.f1.info.features.racereplay.presentation.ui.screen.RaceReplayScreen

fun NavGraphBuilder.raceReplayNavGraph() {
    composable<AppDestination.RaceReplay> {
        RaceReplayScreen()
    }
}
