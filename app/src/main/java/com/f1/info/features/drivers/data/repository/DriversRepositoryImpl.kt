package com.f1.info.features.drivers.data.repository

import com.f1.info.features.drivers.data.remote.DriversApiService
import com.f1.info.features.drivers.data.remote.dto.toDomain
import com.f1.info.features.drivers.domain.model.Driver
import com.f1.info.features.drivers.domain.repository.DriversRepository

class DriversRepositoryImpl(
    private val apiService: DriversApiService
) : DriversRepository {
    override suspend fun getDrivers(sessionKey: Int): Result<List<Driver>> {
        return try {
            val drivers = apiService.getDrivers(sessionKey).map { it.toDomain() }
            Result.success(drivers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
