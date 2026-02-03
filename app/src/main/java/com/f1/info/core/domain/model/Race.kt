package com.f1.info.core.domain.model

data class Race(
    val meetingKey: Int,
    val meetingName: String,
    val meetingOfficialName: String,
    val location: String,
    val countryKey: Int,
    val countryCode: String,
    val countryName: String,
    val circuitKey: Int,
    val circuitShortName: String,
    val dateStart: String,
    val ganPrixName: String,
    val sessionKey: Int,
    val sessionName: String,
    val sessionType: String,
    val date: String
)
