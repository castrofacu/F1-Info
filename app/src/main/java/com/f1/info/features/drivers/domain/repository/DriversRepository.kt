package com.f1.info.features.drivers.domain.repository

import com.f1.info.features.drivers.domain.model.Driver
import kotlinx.coroutines.flow.Flow

interface DriversRepository {
    suspend fun getDrivers(): Flow<Result<List<Driver>>>
}
