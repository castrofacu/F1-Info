package com.f1.info.core.domain.model

import java.io.IOException

sealed interface DomainError {
    data class NetworkError(val cause: IOException) : DomainError
    data class ServerError(val code: Int, val message: String) : DomainError
    data class ParseError(val cause: Throwable) : DomainError
    data class UnknownError(val cause: Throwable) : DomainError
}
