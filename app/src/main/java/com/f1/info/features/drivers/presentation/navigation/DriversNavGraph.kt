package com.f1.info.features.drivers.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.f1.info.core.navigation.AppDestination
import com.f1.info.features.drivers.presentation.ui.screen.DriversScreen

fun NavGraphBuilder.driversNavGraph() {
    composable<AppDestination.Drivers> {
        DriversScreen()
    }
}
