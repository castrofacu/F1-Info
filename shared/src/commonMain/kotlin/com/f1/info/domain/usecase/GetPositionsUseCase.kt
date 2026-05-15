package com.f1.info.domain.usecase

import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Position
import com.f1.info.domain.model.Result
import com.f1.info.domain.repository.PositionsRepository

class GetPositionsUseCase(
    private val positionsRepository: PositionsRepository
) {
    suspend operator fun invoke(sessionKey: Int): Result<List<Position>, DomainError> =
        positionsRepository.getPositions(sessionKey)
}
