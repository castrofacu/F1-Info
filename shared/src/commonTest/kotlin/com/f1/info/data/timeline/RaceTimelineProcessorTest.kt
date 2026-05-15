package com.f1.info.data.timeline

import com.f1.info.test.fixtures.DriverFixtures
import com.f1.info.test.fixtures.PositionFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RaceTimelineProcessorTest {

    private val processor = RaceTimelineProcessor()

    @Test
    fun `given empty positions, when building timeline, then returns empty map`() {
        // Given
        val positions = emptyList<com.f1.info.domain.model.Position>()
        val drivers = listOf(DriverFixtures.hamilton())

        // When
        val result = processor(positions, drivers)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `given empty drivers, when building timeline, then returns empty map`() {
        // Given
        val positions = listOf(PositionFixtures.hamiltonP1AtT1())
        val drivers = emptyList<com.f1.info.domain.model.Driver>()

        // When
        val result = processor(positions, drivers)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `given positions with two unique timestamps, when building timeline, then creates two snapshots`() {
        // Given
        val positions = listOf(
            PositionFixtures.hamiltonP1AtT1(),
            PositionFixtures.hamiltonP2AtT2()
        )
        val drivers = listOf(DriverFixtures.hamilton())

        // When
        val result = processor(positions, drivers)

        // Then
        assertEquals(2, result.size)
        assertTrue(result.containsKey(PositionFixtures.T1))
        assertTrue(result.containsKey(PositionFixtures.T2))
    }

    @Test
    fun `given positions at different timestamps, when building timeline, then snapshots are ordered chronologically`() {
        // Given
        val positions = listOf(
            PositionFixtures.hamiltonP1AtT1(),
            PositionFixtures.hamiltonP2AtT2()
        )
        val drivers = listOf(DriverFixtures.hamilton())

        // When
        val result = processor(positions, drivers)

        // Then
        val keys = result.keys.toList()
        assertEquals(listOf(PositionFixtures.T1, PositionFixtures.T2), keys)
    }

    @Test
    fun `given a driver with no update at T2, when building timeline, then T2 snapshot carries forward T1 position`() {
        // Given
        val positions = listOf(
            PositionFixtures.hamiltonP1AtT1(),
            PositionFixtures.verstappenP2AtT1(),
            PositionFixtures.hamiltonP2AtT2()
        )
        val drivers = listOf(DriverFixtures.hamilton(), DriverFixtures.verstappen())

        // When
        val result = processor(positions, drivers)
        val snapshotAtT2 = result[PositionFixtures.T2]!!

        // Then
        val verstappen = snapshotAtT2.find { it.number == 1 }!!
        assertEquals(2, verstappen.position)
    }

    @Test
    fun `given a driver with no positions at all, when building timeline, then that driver is excluded from snapshots`() {
        // Given — solo hay posiciones para Hamilton; Verstappen no tiene ninguna
        // El processor solo incluye en el snapshot a drivers que tienen al menos una posición
        val positions = listOf(PositionFixtures.hamiltonP1AtT1())
        val drivers = listOf(DriverFixtures.hamilton(), DriverFixtures.verstappen())

        // When
        val result = processor(positions, drivers)
        val snapshot = result[PositionFixtures.T1]!!

        // Then
        assertEquals(1, snapshot.size)
        assertEquals(44, snapshot[0].number)
    }

    @Test
    fun `given drivers with different positions, when building timeline, then snapshot is sorted by position ascending`() {
        // Given
        val positions = listOf(
            PositionFixtures.hamiltonP1AtT1(),
            PositionFixtures.verstappenP2AtT1()
        )
        val drivers = listOf(DriverFixtures.verstappen(), DriverFixtures.hamilton()) // orden invertido a propósito

        // When
        val result = processor(positions, drivers)
        val snapshot = result[PositionFixtures.T1]!!

        // Then — P1 primero, P2 segundo
        assertEquals(1, snapshot[0].position)
        assertEquals(2, snapshot[1].position)
    }
}
