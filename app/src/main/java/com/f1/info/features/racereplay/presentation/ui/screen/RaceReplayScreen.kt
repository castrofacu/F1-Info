package com.f1.info.features.racereplay.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.f1.info.core.ui.components.ErrorComponent
import com.f1.info.core.ui.components.LoadingComponent
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayEffect
import com.f1.info.features.racereplay.presentation.mvi.RaceReplayIntent
import com.f1.info.features.racereplay.presentation.ui.components.DriverPositionCard
import com.f1.info.features.racereplay.presentation.viewmodel.RaceReplayViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaceReplayScreen(
    modifier: Modifier = Modifier,
    viewModel: RaceReplayViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(RaceReplayIntent.LoadRaceData)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is RaceReplayEffect.ShowError -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (state.isPlaying) {
                viewModel.handleIntent(RaceReplayIntent.PlayStop)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Abu Dhabi Grand Prix 2025",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                actions = {
                    if (state.isPlaying) {
                        Text(
                            text = "Time: ${state.currentRaceTime}",
                            modifier = Modifier.padding(end = 16.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handleIntent(RaceReplayIntent.PlayStop) }
            ) {
                Icon(
                    imageVector = if (state.isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (state.isPlaying) "Stop" else "Play"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    )
    { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> LoadingComponent()
                state.error != null -> {
                    ErrorComponent(
                        message = state.error!!,
                        onRetry = { viewModel.handleIntent(RaceReplayIntent.RetryLoad) })
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.drivers, key = { it.number }) { driver ->
                            DriverPositionCard(driver)
                        }
                    }
                }
            }
        }
    }
}
