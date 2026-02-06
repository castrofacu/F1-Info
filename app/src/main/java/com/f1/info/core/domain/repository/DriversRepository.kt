package com.f1.info.core.domain.repository

import com.f1.info.core.domain.model.Driver

interface DriversRepository {
    suspend fun getDrivers(sessionKey: Int): Result<List<Driver>>
}
