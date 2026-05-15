package com.f1.info.data.remote

import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Result
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.SerializationException

suspend fun <T> safeApiCall(block: suspend () -> T): Result<T, DomainError> {
    return try {
        Result.Success(block())
    } catch (e: ClientRequestException) {
        Result.Failure(DomainError.ServerError(e.response.status.value, e.message))
    } catch (e: ServerResponseException) {
        Result.Failure(DomainError.ServerError(e.response.status.value, e.message))
    } catch (e: JsonConvertException) {
        Result.Failure(DomainError.ParseError(e.message))
    } catch (e: SerializationException) {
        Result.Failure(DomainError.ParseError(e.message))
    } catch (e: ConnectTimeoutException) {
        Result.Failure(DomainError.NetworkError(e.message))
    } catch (e: SocketTimeoutException) {
        Result.Failure(DomainError.NetworkError(e.message))
    } catch (e: Exception) {
        Result.Failure(DomainError.UnknownError(e.message))
    }
}
