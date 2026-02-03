package com.f1.info.core.domain.repository

import com.f1.info.core.domain.model.Race

interface RacesRepository {
    suspend fun getRaces(year: Int): Result<List<Race>>
}
