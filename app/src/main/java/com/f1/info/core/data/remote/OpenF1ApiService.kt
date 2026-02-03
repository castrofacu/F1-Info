package com.f1.info.core.data.remote

import com.f1.info.core.data.remote.dto.DriverDto
import com.f1.info.core.data.remote.dto.PositionDto
import com.f1.info.core.data.remote.dto.RaceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenF1ApiService {

    @GET("v1/position")
    suspend fun getPositions(@Query("session_key") sessionKey: Int): List<PositionDto>

    @GET("v1/sessions")
    suspend fun getRaces(@Query("year") year: Int): List<RaceDto>

    @GET("v1/drivers")
    suspend fun getDrivers(@Query("session_key") sessionKey: Int): List<DriverDto>
}
