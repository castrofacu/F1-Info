package com.f1.info.features.drivers.data.remote

import com.f1.info.features.drivers.data.remote.dto.DriverDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DriversApiService {

    @GET("v1/drivers")
    suspend fun getDrivers(@Query("session_key") sessionKey: Int): List<DriverDto>
}