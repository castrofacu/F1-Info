package com.f1.info.core.data.repository

import com.f1.info.core.data.remote.OpenF1ApiService
import com.f1.info.core.data.remote.mapper.toDomain
import com.f1.info.core.domain.model.Position
import com.f1.info.core.domain.model.DomainError
import com.f1.info.core.domain.model.Result
import com.f1.info.core.domain.repository.PositionsRepository
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

class PositionsRepositoryImpl(
    private val apiService: OpenF1ApiService
) : PositionsRepository {
    override suspend fun getPositions(sessionKey: Int): Result<List<Position>, DomainError> {
        return try {
            val positions = apiService.getPositions(sessionKey).map { it.toDomain() }
            Result.Success(positions)
        } catch (e: IOException) {
            Result.Failure(DomainError.NetworkError(e))
        } catch (e: HttpException) {
            Result.Failure(DomainError.ServerError(e.code(), e.message()))
        } catch (e: JsonSyntaxException) {
            Result.Failure(DomainError.ParseError(e))
        } catch (e: Exception) {
            Result.Failure(DomainError.UnknownError(e))
        }
    }
}
