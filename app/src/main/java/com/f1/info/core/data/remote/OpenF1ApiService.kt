package com.f1.info.core.data.remote

import com.f1.info.core.data.remote.dto.PositionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenF1ApiService {

    @GET("v1/position")
    suspend fun getPositions(@Query("session_key") sessionKey: Int): List<PositionDto>
}
