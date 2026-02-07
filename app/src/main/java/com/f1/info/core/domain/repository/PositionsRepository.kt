package com.f1.info.core.domain.repository

import com.f1.info.core.domain.model.Position
import com.f1.info.core.domain.model.Result
import com.f1.info.core.domain.model.DomainError

interface PositionsRepository {
    suspend fun getPositions(sessionKey: Int): Result<List<Position>, DomainError>
}
