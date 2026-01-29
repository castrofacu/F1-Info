package com.f1.info.features.home.data.remote.dto

import com.f1.info.features.home.domain.model.Driver

fun DriverDto.toDomain(): Driver {
    return Driver(
        id = driverId,
        name = "$name $surname",
        number = number,
        teamId = teamId,
        nationality = nationality
    )
}
