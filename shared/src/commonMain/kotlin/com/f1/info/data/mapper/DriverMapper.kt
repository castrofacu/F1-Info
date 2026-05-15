package com.f1.info.data.mapper

import com.f1.info.data.dto.DriverDto
import com.f1.info.domain.model.Driver

fun DriverDto.toDomain(): Driver {
    return Driver(
        number = number,
        broadcastName = broadcastName,
        fullName = fullName,
        firstName = firstName,
        lastName = lastName,
        headshotUrl = headshotUrl,
        teamName = teamName,
        teamColour = "#$teamColour"
    )
}
