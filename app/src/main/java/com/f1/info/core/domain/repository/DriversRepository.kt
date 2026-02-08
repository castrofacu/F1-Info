package com.f1.info.core.domain.repository

import com.f1.info.core.domain.model.Driver
import com.f1.info.core.domain.model.Result
import com.f1.info.core.domain.model.DomainError

interface DriversRepository {
    suspend fun getDrivers(sessionKey: Int): Result<List<Driver>, DomainError>
}
