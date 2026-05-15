package com.f1.info.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PositionDto(
    @SerialName("driver_number")
    val driverNumber: Int,
    @SerialName("position")
    val position: Int,
    @SerialName("date")
    val date: String
)
