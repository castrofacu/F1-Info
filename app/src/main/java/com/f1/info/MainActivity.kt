package com.f1.info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.f1.info.core.navigation.AppDestination
import com.f1.info.core.navigation.rememberNavigationState
import com.f1.info.core.presentation.ui.theme.F1InfoTheme
import com.f1.info.features.drivers.presentation.navigation.driversNavGraph
import com.f1.info.features.racereplay.presentation.navigation.raceReplayNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            F1InfoTheme {
                F1InfoApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun F1InfoApp() {
    val navigationState = rememberNavigationState()
    val navBackStackEntry by navigationState.navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestination.topLevelDestinations.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = currentDestination?.route == destination.route::class.qualifiedName,
                    onClick = { navigationState.navigateToTopLevelDestination(destination) }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navigationState.navController,
                startDestination = AppDestination.Drivers,
                modifier = Modifier.padding(innerPadding)
            ) {
                driversNavGraph()
                raceReplayNavGraph()
            }
        }
    }
}
