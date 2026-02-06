package com.f1.info.features.drivers.presentation.mvi

import com.f1.info.core.domain.model.Driver

sealed interface DriversIntent {
    data object LoadDrivers : DriversIntent
    data object RetryLoad : DriversIntent
    data class OnDriverClick(val driver: Driver) : DriversIntent
}
