package com.f1.info.features.home.domain.repository

import com.f1.info.features.home.domain.model.Driver
import kotlinx.coroutines.flow.Flow

interface DriversRepository {
    suspend fun getDrivers(): Flow<Result<List<Driver>>>
}
