package com.f1.info.features.drivers.presentation.mvi

sealed interface DriversEffect {
    data class NavigateToDriverDetail(val driverNumber: Int) : DriversEffect
    data class ShowError(val message: String) : DriversEffect
}
