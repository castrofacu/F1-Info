package com.f1.info.features.drivers.data.remote.dto

import com.f1.info.features.drivers.domain.model.Driver

fun DriverDto.toDomain(): Driver {
    return Driver(
        id = driverNumber,
        name = fullName,
        number = driverNumber,
        teamName = teamName,
        headshotUrl = headshotUrl,
        teamColour = "#$teamColour"
    )
}
