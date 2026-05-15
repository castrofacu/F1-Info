package com.f1.info.domain.usecase

import com.f1.info.domain.model.Driver
import com.f1.info.domain.model.DriverPosition
import com.f1.info.domain.model.Position
import kotlinx.datetime.Instant

fun interface BuildRaceTimelineUseCase {
    operator fun invoke(
        positions: List<Position>,
        drivers: List<Driver>
    ): Map<Instant, List<DriverPosition>>
}
