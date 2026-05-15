package com.f1.info.domain.usecase

import com.f1.info.domain.model.Driver
import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Result
import com.f1.info.domain.repository.DriversRepository

class GetDriversUseCase(
    private val driversRepository: DriversRepository
) {
    suspend operator fun invoke(sessionKey: Int): Result<List<Driver>, DomainError> =
        driversRepository.getDrivers(sessionKey)
}
