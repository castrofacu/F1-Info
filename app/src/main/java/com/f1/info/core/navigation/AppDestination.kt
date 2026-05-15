package com.f1.info.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed interface AppDestination {
    
    sealed interface TopLevel : AppDestination {
        val label: String
        val icon: ImageVector
        val route: AppDestination
    }
    
    @Serializable
    data object Drivers : TopLevel {
        override val label: String = "Drivers"
        override val icon: ImageVector = Icons.Filled.SportsMotorsports
        override val route: AppDestination = this
    }
    
    @Serializable
    data object RaceReplay : TopLevel {
        override val label: String = "Race Replay"
        override val icon: ImageVector = Icons.Filled.OndemandVideo
        override val route: AppDestination = this
    }
    
    companion object {
        val topLevelDestinations: List<TopLevel> = listOf(
            Drivers,
            RaceReplay
        )
    }
}
