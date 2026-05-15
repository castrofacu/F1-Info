package com.f1.info.domain.model

data class Driver(
    val number: Int,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val broadcastName: String,
    val headshotUrl: String?,
    val teamColour: String,
    val teamName: String
)
