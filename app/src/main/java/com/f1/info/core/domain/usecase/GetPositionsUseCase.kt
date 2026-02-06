package com.f1.info.core.domain.usecase

import com.f1.info.core.domain.repository.PositionsRepository

class GetPositionsUseCase(
    private val positionsRepository: PositionsRepository
) {
    suspend operator fun invoke(sessionKey: Int) = positionsRepository.getPositions(sessionKey)
}
