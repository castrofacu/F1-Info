package com.f1.info.core.domain.model

import java.time.Instant

data class Position(
    val driverNumber: Int,
    val position: Int,
    val date: Instant
)
