package com.f1.info.domain.model

import kotlinx.datetime.Instant

data class Position(
    val driverNumber: Int,
    val position: Int,
    val date: Instant
)
