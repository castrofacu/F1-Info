package com.f1.info.features.drivers.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.f1.info.core.common.AppConstants
import com.f1.info.core.domain.model.fold
import com.f1.info.core.domain.usecase.GetDriversUseCase
import com.f1.info.core.presentation.mvi.BaseViewModel
import com.f1.info.core.presentation.util.ErrorMessageMapper
import com.f1.info.features.drivers.presentation.mvi.DriversEffect
import com.f1.info.features.drivers.presentation.mvi.DriversIntent
import com.f1.info.features.drivers.presentation.mvi.DriversState
import kotlinx.coroutines.launch

class DriversViewModel(
    private val getDriversUseCase: GetDriversUseCase
) : BaseViewModel<DriversState, DriversIntent, DriversEffect>(DriversState()) {

    init {
        handleIntent(DriversIntent.LoadDrivers)
    }

    override fun handleIntent(intent: DriversIntent) {
        when (intent) {
            is DriversIntent.LoadDrivers -> loadDrivers()
            is DriversIntent.RetryLoad -> loadDrivers()
            is DriversIntent.OnDriverClick -> {
                viewModelScope.launch {
                    sendEffect(DriversEffect.NavigateToDriverDetail(intent.driver.number))
                }
            }
        }
    }

    private fun loadDrivers() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            getDriversUseCase(AppConstants.LAST_2025_RACE_SESSION_KEY).fold(
                onSuccess = { drivers ->
                    updateState { copy(isLoading = false, drivers = drivers) }
                },
                onFailure = { error ->
                    val errorMessage = ErrorMessageMapper.map(error)
                    updateState { copy(isLoading = false, error = errorMessage) }
                    sendEffect(DriversEffect.ShowError(errorMessage))
                }
            )
        }
    }
}
