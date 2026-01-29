package com.f1.info.features.drivers.data.remote.dto

data class DriverDto(
    val driverId: String,
    val name: String,
    val surname: String,
    val nationality: String,
    val birthday: String,
    val number: Int,
    val shortName: String,
    val url: String,
    val teamId: String
)
