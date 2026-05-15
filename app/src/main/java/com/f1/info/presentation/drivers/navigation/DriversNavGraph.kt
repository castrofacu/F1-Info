package com.f1.info.presentation.drivers.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.info.presentation.core.navigation.AppDestination
import com.f1.info.presentation.drivers.ui.DriversScreen

fun NavGraphBuilder.driversNavGraph() {
    composable<AppDestination.Drivers> {
        DriversScreen()
    }
}
