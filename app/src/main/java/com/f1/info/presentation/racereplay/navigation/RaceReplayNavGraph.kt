package com.f1.info.presentation.racereplay.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.info.presentation.core.navigation.AppDestination
import com.f1.info.presentation.racereplay.ui.RaceReplayScreen

fun NavGraphBuilder.raceReplayNavGraph() {
    composable<AppDestination.RaceReplay> {
        RaceReplayScreen()
    }
}
