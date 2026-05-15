package com.f1.info.data.timeline

import com.f1.info.domain.model.Driver
import com.f1.info.domain.model.Position
import com.f1.info.domain.usecase.BuildRaceTimelineUseCase
import com.f1.info.features.racereplay.model.DriverPosition
import kotlinx.datetime.Instant

// THIS ASSUMES TIMESTAMPS ARE IN ORDER
class RaceTimelineProcessor : BuildRaceTimelineUseCase {

    override operator fun invoke(
        positions: List<Position>,
        drivers: List<Driver>
    ): Map<Instant, List<DriverPosition>> {
        if (positions.isEmpty() || drivers.isEmpty()) {
            return emptyMap()
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
        val timestamps = mutableListOf<Instant>()
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
    ): Map<Instant, List<DriverPosition>> {
        return timestamps.associateWith { timestamp ->
            cursors.map { cursor ->
                val currentPos = cursor.advanceTo(timestamp)
                createDriverPosition(cursor.driver, currentPos?.position)
            }.sortedWith(compareBy(nullsLast()) { it.position })
        }.toSortedMap()
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
        private var currentIndex = -1

        fun advanceTo(timestamp: Instant): Position? {
            while (currentIndex + 1 < positions.size &&
                positions[currentIndex + 1].date <= timestamp) {
                currentIndex++
            }
            return if (currentIndex >= 0) positions[currentIndex] else null
        }
    }
}
