package com.f1.info.features.drivers.presentation.mvi

sealed interface DriversEffect {
    data class NavigateToDriverDetail(val driverId: String) : DriversEffect
    data class ShowError(val message: String) : DriversEffect
}
