package com.f1.info.data.mapper

import com.f1.info.data.dto.PositionDto
import com.f1.info.domain.model.Position
import kotlinx.datetime.Instant

fun PositionDto.toDomain(): Position {
    return Position(
        driverNumber = driverNumber,
        position = position,
        date = Instant.parse(date)
    )
}
