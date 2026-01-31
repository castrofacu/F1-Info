package com.f1.info.features.drivers.presentation.mvi

import com.f1.info.features.drivers.domain.model.Driver

data class DriversState(
    val isLoading: Boolean = false,
    val drivers: List<Driver> = emptyList(),
    val error: String? = null
)
