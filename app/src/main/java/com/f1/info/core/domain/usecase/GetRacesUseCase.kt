package com.f1.info.core.domain.usecase

import com.f1.info.core.domain.repository.RacesRepository

class GetRacesUseCase(
    private val racesRepository: RacesRepository
) {
    suspend operator fun invoke(year: Int) = racesRepository.getRaces(year)
}
