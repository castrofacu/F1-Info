package com.f1.info.domain.model

sealed interface DomainError {
    data class NetworkError(val message: String?) : DomainError
    data class ServerError(val code: Int, val message: String) : DomainError
    data class ParseError(val message: String?) : DomainError
    data class UnknownError(val message: String?) : DomainError
}
