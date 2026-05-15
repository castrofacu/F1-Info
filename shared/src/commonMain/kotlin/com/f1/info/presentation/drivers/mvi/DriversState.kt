package com.f1.info.presentation.drivers.mvi

import com.f1.info.domain.model.Driver

data class DriversState(
    val isLoading: Boolean = false,
    val drivers: List<Driver> = emptyList(),
    val error: String? = null
)
