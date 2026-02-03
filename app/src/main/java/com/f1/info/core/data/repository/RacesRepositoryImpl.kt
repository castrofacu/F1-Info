package com.f1.info.core.data.repository

import com.f1.info.core.data.remote.OpenF1ApiService
import com.f1.info.core.data.remote.mapper.toDomain
import com.f1.info.core.domain.model.Race
import com.f1.info.core.domain.repository.RacesRepository

class RacesRepositoryImpl(
    private val apiService: OpenF1ApiService
) : RacesRepository {
    override suspend fun getRaces(year: Int): Result<List<Race>> {
        return try {
            val races = apiService.getRaces(year).map { it.toDomain() }
            Result.success(races)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
