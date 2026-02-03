package com.f1.info.core.data.remote.mapper

import com.f1.info.core.data.remote.dto.RaceDto
import com.f1.info.core.domain.model.Race

fun RaceDto.toDomain(): Race {
    return Race(
        meetingKey = meetingKey,
        meetingName = meetingName,
        meetingOfficialName = meetingOfficialName,
        location = location,
        countryKey = countryKey,
        countryCode = countryCode,
        countryName = countryName,
        circuitKey = circuitKey,
        circuitShortName = circuitShortName,
        dateStart = dateStart,
        ganPrixName = ganPrixName,
        sessionKey = sessionKey,
        sessionName = sessionName,
        sessionType = sessionType,
        date = date
    )
}
