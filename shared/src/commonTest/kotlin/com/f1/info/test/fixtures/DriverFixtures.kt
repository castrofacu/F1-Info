package com.f1.info.test.fixtures

import com.f1.info.data.dto.DriverDto
import com.f1.info.domain.model.Driver

object DriverFixtures {

    fun hamilton() = Driver(
        number = 44,
        fullName = "Lewis Hamilton",
        firstName = "Lewis",
        lastName = "Hamilton",
        broadcastName = "HAM",
        headshotUrl = "https://example.com/ham.png",
        teamName = "Mercedes",
        teamColour = "#6CD3BF"
    )

    fun verstappen() = Driver(
        number = 1,
        fullName = "Max Verstappen",
        firstName = "Max",
        lastName = "Verstappen",
        broadcastName = "VER",
        headshotUrl = "https://example.com/ver.png",
        teamName = "Red Bull Racing",
        teamColour = "#3671C6"
    )

    fun hamiltonDto() = DriverDto(
        number = 44,
        fullName = "Lewis Hamilton",
        firstName = "Lewis",
        lastName = "Hamilton",
        broadcastName = "HAM",
        headshotUrl = "https://example.com/ham.png",
        teamName = "Mercedes",
        teamColour = "6CD3BF"
    )

    fun verstappenDto() = DriverDto(
        number = 1,
        fullName = "Max Verstappen",
        firstName = "Max",
        lastName = "Verstappen",
        broadcastName = "VER",
        headshotUrl = "https://example.com/ver.png",
        teamName = "Red Bull Racing",
        teamColour = "3671C6"
    )

    fun hamiltonJson() = """
        {
          "driver_number": 44,
          "full_name": "Lewis Hamilton",
          "first_name": "Lewis",
          "last_name": "Hamilton",
          "broadcast_name": "HAM",
          "headshot_url": "https://example.com/ham.png",
          "team_name": "Mercedes",
          "team_colour": "6CD3BF"
        }
    """.trimIndent()

    fun verstappenJson() = """
        {
          "driver_number": 1,
          "full_name": "Max Verstappen",
          "first_name": "Max",
          "last_name": "Verstappen",
          "broadcast_name": "VER",
          "headshot_url": "https://example.com/ver.png",
          "team_name": "Red Bull Racing",
          "team_colour": "3671C6"
        }
    """.trimIndent()

    fun allDriversJson() = "[${hamiltonJson()}, ${verstappenJson()}]"

    fun emptyJson() = "[]"
}
