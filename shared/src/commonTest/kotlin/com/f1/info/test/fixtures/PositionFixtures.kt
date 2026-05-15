package com.f1.info.test.fixtures

import com.f1.info.data.dto.PositionDto
import com.f1.info.domain.model.Position
import kotlinx.datetime.Instant

object PositionFixtures {

    val T1: Instant = Instant.parse("2023-09-03T13:00:00Z")
    val T2: Instant = Instant.parse("2023-09-03T13:05:00Z")
    val T3: Instant = Instant.parse("2023-09-03T13:10:00Z")

    fun hamiltonP1AtT1() = Position(driverNumber = 44, position = 1, date = T1)
    fun hamiltonP2AtT2() = Position(driverNumber = 44, position = 2, date = T2)

    fun verstappenP2AtT1() = Position(driverNumber = 1, position = 2, date = T1)
    fun verstappenP1AtT2() = Position(driverNumber = 1, position = 1, date = T2)

    fun hamiltonDto() = PositionDto(
        driverNumber = 44,
        position = 1,
        date = "2023-09-03T13:00:00+00:00"
    )

    fun hamiltonDtoWithMillis() = PositionDto(
        driverNumber = 44,
        position = 1,
        date = "2023-09-03T13:00:00.123+00:00"
    )

    fun hamiltonJson() = """
        {
          "driver_number": 44,
          "position": 1,
          "date": "2023-09-03T13:00:00+00:00"
        }
    """.trimIndent()

    fun verstappenJson() = """
        {
          "driver_number": 1,
          "position": 2,
          "date": "2023-09-03T13:00:00+00:00"
        }
    """.trimIndent()

    fun allPositionsJson() = "[${hamiltonJson()}, ${verstappenJson()}]"

    fun emptyJson() = "[]"
}
