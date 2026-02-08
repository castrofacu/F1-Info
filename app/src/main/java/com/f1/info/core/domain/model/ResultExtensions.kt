package com.f1.info.core.domain.model

inline fun <T, E, R> Result<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: (E) -> R
): R = when (this) {
    is Result.Success -> onSuccess(value)
    is Result.Failure -> onFailure(error)
}

fun <T, E> Result<T, E>.getOrNull(): T? = when (this) {
    is Result.Success -> value
    is Result.Failure -> null
}

fun <T, E> Result<T, E>.getOrElse(default: T): T = when (this) {
    is Result.Success -> value
    is Result.Failure -> default
}

fun <T, E> Result<T, E>.getOrElse(defaultValue: (E) -> T): T = when (this) {
    is Result.Success -> value
    is Result.Failure -> defaultValue(error)
}

inline fun <T, E, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> = when (this) {
    is Result.Success -> Result.Success(transform(value))
    is Result.Failure -> Result.Failure(error)
}

inline fun <T, E, R> Result<T, E>.mapError(transform: (E) -> R): Result<T, R> = when (this) {
    is Result.Success -> Result.Success(value)
    is Result.Failure -> Result.Failure(transform(error))
}

val <T, E> Result<T, E>.isSuccess: Boolean
    get() = this is Result.Success

val <T, E> Result<T, E>.isFailure: Boolean
    get() = this is Result.Failure
