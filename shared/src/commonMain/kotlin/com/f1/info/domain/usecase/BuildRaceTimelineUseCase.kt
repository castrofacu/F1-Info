package com.f1.info.domain.usecase

import com.f1.info.domain.model.Driver
import com.f1.info.domain.model.Position
import com.f1.info.features.racereplay.model.DriverPosition
import kotlinx.datetime.Instant

fun interface BuildRaceTimelineUseCase {
    operator fun invoke(
        positions: List<Position>,
        drivers: List<Driver>
    ): Map<Instant, List<DriverPosition>>
}
