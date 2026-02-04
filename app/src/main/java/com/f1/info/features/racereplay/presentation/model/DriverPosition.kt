package com.f1.info.features.racereplay.presentation.model

data class DriverPosition(
    val number: Int,
    val name: String,
    val teamName: String,
    val headshotUrl: String?,
    val teamColour: String,
    val position: Int
)
