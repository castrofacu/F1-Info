package com.f1.info.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PositionDto(
    @SerializedName("driver_number")
    val driverNumber: Int,
    @SerializedName("position")
    val position: Int,
    @SerializedName("date")
    val date: String
)
