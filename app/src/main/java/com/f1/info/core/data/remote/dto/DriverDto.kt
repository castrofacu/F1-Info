package com.f1.info.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DriverDto(
    @SerializedName("driver_number")
    val number: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("broadcast_name")
    val broadcastName: String,
    @SerializedName("headshot_url")
    val headshotUrl: String?,
    @SerializedName("team_name")
    val teamName: String,
    @SerializedName("team_colour")
    val teamColour: String,
)
