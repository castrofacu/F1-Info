package com.f1.info.features.drivers.data.remote

import com.f1.info.features.drivers.data.remote.dto.DriversResponse
import retrofit2.http.GET

interface DriversApiService {
    @GET("api/current/drivers")
    suspend fun getDrivers(): DriversResponse
}
