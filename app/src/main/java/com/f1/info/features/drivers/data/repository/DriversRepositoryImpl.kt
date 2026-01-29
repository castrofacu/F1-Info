package com.f1.info.features.drivers.data.repository

import com.f1.info.features.drivers.data.remote.DriversApiService
import com.f1.info.features.drivers.data.remote.dto.toDomain
import com.f1.info.features.drivers.domain.model.Driver
import com.f1.info.features.drivers.domain.repository.DriversRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DriversRepositoryImpl(
    private val apiService: DriversApiService
) : DriversRepository {
    override suspend fun getDrivers(): Flow<Result<List<Driver>>> = flow {
        try {
            val drivers = apiService.getDrivers().drivers.map { it.toDomain() }
            emit(Result.success(drivers))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
