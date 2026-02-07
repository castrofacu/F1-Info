package com.f1.info.core.data.repository

import com.f1.info.core.data.remote.OpenF1ApiService
import com.f1.info.core.data.remote.mapper.toDomain
import com.f1.info.core.domain.model.Driver
import com.f1.info.core.domain.model.DomainError
import com.f1.info.core.domain.model.Result
import com.f1.info.core.domain.repository.DriversRepository
import retrofit2.HttpException
import java.io.IOException

class DriversRepositoryImpl(
    private val apiService: OpenF1ApiService
) : DriversRepository {
    override suspend fun getDrivers(sessionKey: Int): Result<List<Driver>, DomainError> {
        return try {
            val drivers = apiService.getDrivers(sessionKey).map { it.toDomain() }
            Result.Success(drivers)
        } catch (e: IOException) {
            Result.Failure(DomainError.NetworkError(e))
        } catch (e: HttpException) {
            Result.Failure(DomainError.ServerError(e.code(), e.message()))
        } catch (e: Exception) {
            Result.Failure(DomainError.UnknownError(e))
        }
    }
}
