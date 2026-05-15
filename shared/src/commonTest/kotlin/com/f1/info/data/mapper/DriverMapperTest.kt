package com.f1.info.data.mapper

import com.f1.info.test.fixtures.DriverFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DriverMapperTest {

    @Test
    fun `given a full DriverDto, when mapped to domain, then all fields are mapped correctly`() {
        // Given
        val dto = DriverFixtures.hamiltonDto()

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(44, result.number)
        assertEquals("Lewis Hamilton", result.fullName)
        assertEquals("Lewis", result.firstName)
        assertEquals("Hamilton", result.lastName)
        assertEquals("HAM", result.broadcastName)
        assertEquals("https://example.com/ham.png", result.headshotUrl)
        assertEquals("Mercedes", result.teamName)
    }

    @Test
    fun `given a DriverDto with colour without hash, when mapped to domain, then hash is prepended`() {
        // Given
        val dto = DriverFixtures.hamiltonDto() // teamColour = "6CD3BF"

        // When
        val result = dto.toDomain()

        // Then
        assertEquals("#6CD3BF", result.teamColour)
    }

    @Test
    fun `given a DriverDto with null headshot url, when mapped to domain, then headshot url is null`() {
        // Given
        val dto = DriverFixtures.hamiltonDto().copy(headshotUrl = null)

        // When
        val result = dto.toDomain()

        // Then
        assertNull(result.headshotUrl)
    }
}
