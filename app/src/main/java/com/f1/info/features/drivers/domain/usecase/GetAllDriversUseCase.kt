package com.f1.info.features.drivers.domain.usecase

import com.f1.info.features.drivers.domain.repository.DriversRepository

class GetAllDriversUseCase(
    private val driversRepository: DriversRepository
) {
    suspend operator fun invoke() = driversRepository.getDrivers()
}
