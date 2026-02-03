package com.f1.info.core.data.repository

import com.f1.info.core.data.remote.OpenF1ApiService
import com.f1.info.core.data.remote.mapper.toDomain
import com.f1.info.core.domain.model.Position
import com.f1.info.core.domain.repository.PositionsRepository

class PositionsRepositoryImpl(
    private val apiService: OpenF1ApiService
) : PositionsRepository {
    override suspend fun getPositions(sessionKey: Int): Result<List<Position>> {
        return try {
            val positions = apiService.getPositions(sessionKey).map { it.toDomain() }
            Result.success(positions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
