package com.f1.info.features.drivers.domain.repository

import com.f1.info.features.drivers.domain.model.Driver

interface DriversRepository {
    suspend fun getDrivers(sessionKey: Int): Result<List<Driver>>
}
