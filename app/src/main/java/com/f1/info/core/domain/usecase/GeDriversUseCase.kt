package com.f1.info.core.domain.usecase

import com.f1.info.core.domain.repository.DriversRepository

class GeDriversUseCase(
    private val driversRepository: DriversRepository
) {
    suspend operator fun invoke(sessionKey: Int) = driversRepository.getDrivers(sessionKey)
}
