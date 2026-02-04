package com.f1.info.core.data.remote.mapper

import com.f1.info.core.data.remote.dto.PositionDto
import com.f1.info.core.domain.model.Position
import java.time.Instant

fun PositionDto.toDomain(): Position {
    return Position(
        driverNumber = driverNumber,
        position = position,
        date = Instant.parse(date)
    )
}
