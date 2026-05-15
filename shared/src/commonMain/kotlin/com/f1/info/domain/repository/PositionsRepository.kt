package com.f1.info.domain.repository

import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Position
import com.f1.info.domain.model.Result

interface PositionsRepository {
    suspend fun getPositions(sessionKey: Int): Result<List<Position>, DomainError>
}
