package com.f1.info.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RaceDto(
    @SerializedName("meeting_key")
    val meetingKey: Int,
    @SerializedName("meeting_name")
    val meetingName: String,
    @SerializedName("meeting_official_name")
    val meetingOfficialName: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("country_key")
    val countryKey: Int,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("country_name")
    val countryName: String,
    @SerializedName("circuit_key")
    val circuitKey: Int,
    @SerializedName("circuit_short_name")
    val circuitShortName: String,
    @SerializedName("date_start")
    val dateStart: String,
    @SerializedName("gmt_offset")
    val gmtOffset: String,
    @SerializedName("gan_prix_name")
    val ganPrixName: String,
    @SerializedName("session_key")
    val sessionKey: Int,
    @SerializedName("session_name")
    val sessionName: String,
    @SerializedName("session_type")
    val sessionType: String,
    @SerializedName("date")
    val date: String
)
