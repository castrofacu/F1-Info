package com.f1.info.data.remote

import com.f1.info.data.dto.DriverDto
import com.f1.info.data.dto.PositionDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://api.openf1.org/"

class OpenF1Client(private val httpClient: HttpClient) {

    suspend fun getDrivers(sessionKey: Int): List<DriverDto> =
        httpClient.get("${BASE_URL}v1/drivers") {
            parameter("session_key", sessionKey)
        }.body()

    suspend fun getPositions(sessionKey: Int): List<PositionDto> =
        httpClient.get("${BASE_URL}v1/position") {
            parameter("session_key", sessionKey)
        }.body()
}
