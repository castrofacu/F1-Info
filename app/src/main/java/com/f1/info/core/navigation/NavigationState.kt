package com.f1.info.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Stable
class NavigationState(
    val navController: NavHostController
) {
    fun navigateToTopLevelDestination(destination: AppDestination.TopLevel) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    
    fun navigate(destination: AppDestination) {
        navController.navigate(destination)
    }
    
    fun navigateBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberNavigationState(
    navController: NavHostController = rememberNavController()
): NavigationState {
    return remember(navController) {
        NavigationState(navController)
    }
}
