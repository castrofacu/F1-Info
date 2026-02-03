package com.f1.info.core.data.remote.mapper

import com.f1.info.core.data.remote.dto.DriverDto
import com.f1.info.core.domain.model.Driver

fun DriverDto.toDomain(): Driver {
    return Driver(
        driverNumber = driverNumber,
        broadcastName = broadcastName,
        fullName = fullName,
        firstName = firstName,
        lastName = lastName,
        headshotUrl = headshotUrl,
        teamName = teamName,
        teamColour = teamColour
    )
}
