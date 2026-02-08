package com.f1.info.core.domain.usecase

import com.f1.info.core.domain.model.Position
import com.f1.info.core.domain.model.DomainError
import com.f1.info.core.domain.model.Result
import com.f1.info.core.domain.repository.PositionsRepository

class GetPositionsUseCase(
    private val positionsRepository: PositionsRepository
) {
    suspend operator fun invoke(sessionKey: Int): Result<List<Position>, DomainError> =
        positionsRepository.getPositions(sessionKey)
}
