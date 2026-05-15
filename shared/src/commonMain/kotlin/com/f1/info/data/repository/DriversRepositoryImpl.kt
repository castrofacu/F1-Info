package com.f1.info.data.repository

import com.f1.info.data.mapper.toDomain
import com.f1.info.data.remote.OpenF1Client
import com.f1.info.data.remote.safeApiCall
import com.f1.info.domain.model.Driver
import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Result
import com.f1.info.domain.repository.DriversRepository

class DriversRepositoryImpl(
    private val client: OpenF1Client
) : DriversRepository {
    override suspend fun getDrivers(sessionKey: Int): Result<List<Driver>, DomainError> =
        safeApiCall { client.getDrivers(sessionKey).map { it.toDomain() } }
}
