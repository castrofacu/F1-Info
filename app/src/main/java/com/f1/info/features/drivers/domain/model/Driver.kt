package com.f1.info.features.drivers.domain.model

data class Driver(
    val id: Int,
    val name: String,
    val number: Int,
    val teamName: String,
    val headshotUrl: String?,
    val teamColour: String
)
