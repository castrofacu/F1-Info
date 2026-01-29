package com.f1.info.features.drivers.data.remote

import com.f1.info.features.drivers.data.remote.dto.DriverDto
import retrofit2.http.GET

interface DriversApiService {
    @GET("v1/drivers?session_key=9839")
    suspend fun getDrivers(): List<DriverDto>
}
