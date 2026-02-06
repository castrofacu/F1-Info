package com.f1.info.core.domain.repository

import com.f1.info.core.domain.model.Position

interface PositionsRepository {
    suspend fun getPositions(sessionKey: Int): Result<List<Position>>
}
