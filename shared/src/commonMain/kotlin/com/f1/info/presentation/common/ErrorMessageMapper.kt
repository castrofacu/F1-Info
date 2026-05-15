package com.f1.info.presentation.common

import com.f1.info.domain.model.DomainError

object ErrorMessageMapper {
    fun map(error: DomainError): String {
        return when (error) {
            is DomainError.NetworkError ->
                "Network error. Please check your connection and try again."
            is DomainError.ServerError -> when (error.code) {
                500, 502, 503, 504 ->
                    "Service temporarily unavailable. Please try again later."
                404 ->
                    "The requested data was not found."
                else ->
                    "Server error occurred. Please try again."
            }
            is DomainError.ParseError ->
                "Data format error. Please update the app or try again later."
            is DomainError.UnknownError ->
                "An unexpected error occurred. Please try again."
        }
    }
}