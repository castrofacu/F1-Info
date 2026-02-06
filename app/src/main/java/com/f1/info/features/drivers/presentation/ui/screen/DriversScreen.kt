package com.f1.info.features.drivers.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.f1.info.core.domain.model.Driver
import com.f1.info.core.ui.theme.F1InfoTheme
import com.f1.info.features.drivers.presentation.mvi.DriversEffect
import com.f1.info.features.drivers.presentation.mvi.DriversIntent
import com.f1.info.features.drivers.presentation.mvi.DriversState
import com.f1.info.features.drivers.presentation.ui.components.DriverCard
import com.f1.info.features.drivers.presentation.ui.components.ErrorComponent
import com.f1.info.features.drivers.presentation.ui.components.Loading
import com.f1.info.features.drivers.presentation.viewmodel.DriversViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DriversScreen(
    modifier: Modifier = Modifier,
    viewModel: DriversViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is DriversEffect.ShowError -> {
                    snackbarHostState.showSnackbar(it.message)
                }
                is DriversEffect.NavigateToDriverDetail -> {
                    // Handle navigation here
                }
            }
        }
    }

    DriversScreenContent(
        modifier = modifier,
        state = state,
        snackbarHostState = snackbarHostState,
        onRetry = { viewModel.handleIntent(DriversIntent.RetryLoad) }
    )
}

@Composable
private fun DriversScreenContent(
    modifier: Modifier = Modifier,
    state: DriversState,
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        when {
            state.isLoading -> Loading(modifier = Modifier.padding(innerPadding))
            state.error != null -> ErrorComponent(
                message = state.error,
                modifier = Modifier.padding(innerPadding),
                onRetry = onRetry
            )
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                ) {
                    items(state.drivers) { driver ->
                        DriverCard(driver = driver)
                    }
                }
            }
        }
    }
}

@Preview(name = "Success State", showBackground = true)
@Composable
private fun DriversScreenSuccessPreview() {
    F1InfoTheme {
        DriversScreenContent(
            state = DriversState(
                isLoading = false,
                drivers = listOf(
                    Driver(
                        fullName = "Max VERSTAPPEN",
                        number = 1,
                        teamName = "Red Bull Racing",
                        headshotUrl = "https://www.formula1.com/content/dam/fom-website/drivers/M/MAXVER01_Max_Verstappen/maxver01.png.transform/1col/image.png",
                        teamColour = "#3671C6",
                        broadcastName = "M VERSTAPPEN",
                        firstName = "Max",
                        lastName = "Verstappen"
                    ),
                    Driver(
                        fullName = "Lando NORRIS",
                        number = 4,
                        teamName = "McLaren",
                        headshotUrl = "https://www.formula1.com/content/dam/fom-website/drivers/L/LANNOR01_Lando_Norris/lannor01.png.transform/1col/image.png",
                        teamColour = "#F58020",
                        broadcastName = "L NORRIS",
                        firstName = "Lando",
                        lastName = "Norris"
                    )
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onRetry = {}
        )
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
private fun DriversScreenLoadingPreview() {
    F1InfoTheme {
        DriversScreenContent(
            state = DriversState(isLoading = true),
            snackbarHostState = remember { SnackbarHostState() },
            onRetry = {}
        )
    }
}

@Preview(name = "Error State", showBackground = true)
@Composable
private fun DriversScreenErrorPreview() {
    F1InfoTheme {
        DriversScreenContent(
            state = DriversState(error = "Could not connect to the server."),
            snackbarHostState = remember { SnackbarHostState() },
            onRetry = {}
        )
    }
}
