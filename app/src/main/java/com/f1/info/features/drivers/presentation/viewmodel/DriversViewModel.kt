package com.f1.info.features.drivers.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1.info.features.drivers.domain.usecase.GetAllDriversUseCase
import com.f1.info.features.drivers.presentation.mvi.DriversEffect
import com.f1.info.features.drivers.presentation.mvi.DriversIntent
import com.f1.info.features.drivers.presentation.mvi.DriversState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DriversViewModel(
    private val getAllDriversUseCase: GetAllDriversUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DriversState())
    val state = _state.asStateFlow()

    private val _effect = Channel<DriversEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(DriversIntent.LoadDrivers)
    }

    fun handleIntent(intent: DriversIntent) {
        when (intent) {
            is DriversIntent.LoadDrivers -> loadDrivers()
            is DriversIntent.RetryLoad -> loadDrivers()
            is DriversIntent.OnDriverClick -> {
                viewModelScope.launch {
                    _effect.send(DriversEffect.NavigateToDriverDetail(intent.driver.id))
                }
            }
        }
    }

    private fun loadDrivers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            getAllDriversUseCase()
                .onEach { result ->
                    result.fold(
                        onSuccess = { drivers ->
                            _state.value = _state.value.copy(isLoading = false, drivers = drivers)
                        },
                        onFailure = { error ->
                            val errorMessage = error.message ?: "An unexpected error occurred"
                            _state.value = _state.value.copy(isLoading = false, error = errorMessage)
                            _effect.send(DriversEffect.ShowError(errorMessage))
                        }
                    )
                }
                .catch { error ->
                    val errorMessage = error.message ?: "An unexpected error occurred"
                    _state.value = _state.value.copy(isLoading = false, error = errorMessage)
                    _effect.send(DriversEffect.ShowError(errorMessage))
                }
                .launchIn(viewModelScope)
        }
    }
}
