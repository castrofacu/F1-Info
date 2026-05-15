package com.f1.info.data.mapper

import com.f1.info.test.fixtures.PositionFixtures
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class PositionMapperTest {

    @Test
    fun `given a PositionDto with UTC offset, when mapped to domain, then all fields are mapped correctly`() {
        // Given
        val dto = PositionFixtures.hamiltonDto()

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(44, result.driverNumber)
        assertEquals(1, result.position)

        assertEquals(Instant.parse("2023-09-03T13:00:00Z"), result.date)
    }

    @Test
    fun `given a PositionDto with milliseconds in date, when mapped to domain, then milliseconds are preserved`() {
        // Given
        val dto = PositionFixtures.hamiltonDtoWithMillis()

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(Instant.parse("2023-09-03T13:00:00.123Z"), result.date)
    }
}
