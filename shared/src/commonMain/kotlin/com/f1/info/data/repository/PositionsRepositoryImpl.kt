package com.f1.info.data.repository

import com.f1.info.data.mapper.toDomain
import com.f1.info.data.remote.OpenF1Client
import com.f1.info.data.remote.safeApiCall
import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Position
import com.f1.info.domain.model.Result
import com.f1.info.domain.repository.PositionsRepository

class PositionsRepositoryImpl(
    private val client: OpenF1Client
) : PositionsRepository {
    override suspend fun getPositions(sessionKey: Int): Result<List<Position>, DomainError> =
        safeApiCall { client.getPositions(sessionKey).map { it.toDomain() } }
}
