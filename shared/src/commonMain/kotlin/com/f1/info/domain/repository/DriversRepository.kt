package com.f1.info.domain.repository

import com.f1.info.domain.model.Driver
import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Result

interface DriversRepository {
    suspend fun getDrivers(sessionKey: Int): Result<List<Driver>, DomainError>
}
