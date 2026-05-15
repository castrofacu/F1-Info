package com.f1.info.presentation.drivers.mvi

sealed interface DriversEffect {
    data class NavigateToDriverDetail(val driverNumber: Int) : DriversEffect
    data class ShowError(val message: String) : DriversEffect
}
