package com.f1.info.presentation.drivers.mvi

import com.f1.info.domain.model.Driver

sealed interface DriversIntent {
    data object LoadDrivers : DriversIntent
    data object RetryLoad : DriversIntent
    data class OnDriverClick(val driver: Driver) : DriversIntent
}
