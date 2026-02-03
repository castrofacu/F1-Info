package com.f1.info.core.domain.model

data class Driver(
    val driverNumber: Int,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val broadcastName: String,
    val headshotUrl: String?,
    val teamColour: String,
    val teamName: String
)
