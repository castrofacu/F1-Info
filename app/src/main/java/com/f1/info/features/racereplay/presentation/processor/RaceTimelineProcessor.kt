package com.f1.info.features.racereplay.presentation.processor

import com.f1.info.core.domain.model.Driver
import com.f1.info.core.domain.model.Position
import com.f1.info.features.racereplay.presentation.model.DriverPosition
import java.time.Instant
import java.util.TreeMap
import java.util.ArrayList

// THIS ASSUMES TIMESTAMPS ARE IN ORDER
class RaceTimelineProcessor {

    fun buildTimeline(
        positions: List<Position>,
        drivers: List<Driver>
    ): TreeMap<Instant, List<DriverPosition>> {
        if (positions.isEmpty() || drivers.isEmpty()) {
            return TreeMap()
        }

        val cursors = createDriverCursors(positions, drivers)
        val uniqueTimestamps = extractTimestampsSequentially(positions)

        return generateSnapshots(uniqueTimestamps, cursors)
    }

    private fun createDriverCursors(
        positions: List<Position>,
        drivers: List<Driver>
    ): List<DriverCursor> {
        val driversByNumber = drivers.associateBy { it.number }

        return positions
            .groupBy { it.driverNumber }
            .mapNotNull { (number, driverPositions) ->
                driversByNumber[number]?.let { driver ->
                    DriverCursor(driver, driverPositions)
                }
            }
    }

    private fun extractTimestampsSequentially(positions: List<Position>): List<Instant> {
        val timestamps = ArrayList<Instant>(positions.size)
        var lastTime: Instant? = null

        for (pos in positions) {
            if (pos.date != lastTime) {
                timestamps.add(pos.date)
                lastTime = pos.date
            }
        }
        return timestamps
    }

    private fun generateSnapshots(
        timestamps: List<Instant>,
        cursors: List<DriverCursor>
    ): TreeMap<Instant, List<DriverPosition>> {
        return timestamps.associateWithTo(TreeMap()) { timestamp ->
            val snapshot = cursors.map { cursor ->
                val currentPos = cursor.advanceTo(timestamp)
                createDriverPosition(cursor.driver, currentPos?.position)
            }.sortedWith(compareBy(nullsLast()) { it.position })

            snapshot
        }
    }

    private fun createDriverPosition(driver: Driver, position: Int?): DriverPosition {
        return DriverPosition(
            number = driver.number,
            name = driver.fullName,
            teamName = driver.teamName,
            headshotUrl = driver.headshotUrl,
            teamColour = driver.teamColour,
            position = position
        )
    }

    private class DriverCursor(
        val driver: Driver,
        private val positions: List<Position>
    ) {
        // Tracks the index of the last valid known position
        // Starts at -1 meaning "race hasn't started for this driver"
        private var currentIndex = -1

        fun advanceTo(timestamp: Instant): Position? {
            // Look ahead to the next position
            while (currentIndex + 1 < positions.size &&
                positions[currentIndex + 1].date <= timestamp) {
                currentIndex++
            }

            // Return valid position if we have started, otherwise null
            return if (currentIndex >= 0) positions[currentIndex] else null
        }
    }
}
