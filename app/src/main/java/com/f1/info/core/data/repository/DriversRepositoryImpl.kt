package com.f1.info.core.data.repository

import com.f1.info.core.data.remote.OpenF1ApiService
import com.f1.info.core.data.remote.mapper.toDomain
import com.f1.info.core.domain.model.Driver
import com.f1.info.core.domain.repository.DriversRepository

class DriversRepositoryImpl(
    private val apiService: OpenF1ApiService
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
