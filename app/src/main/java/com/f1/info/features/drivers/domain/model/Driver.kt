package com.f1.info.features.drivers.domain.model

data class Driver(
    val id: String,
    val name: String,
    val number: Int,
    val teamId: String,
    val nationality: String
)
