package com.f1.info.data.repository

import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Result
import com.f1.info.test.fixtures.DriverFixtures
import com.f1.info.test.fixtures.MockHttpClientFactory
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DriversRepositoryTest {

    @Test
    fun `given a valid API response, when getDrivers is called, then returns mapped drivers`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(DriverFixtures.allDriversJson())
        val repo = DriversRepositoryImpl(client)

        // When
        val result = repo.getDrivers(sessionKey = 9158)

        // Then
        assertIs<Result.Success<*>>(result)
        val drivers = (result as Result.Success).value
        assertEquals(2, drivers.size)
        assertEquals(44, drivers[0].number)
        assertEquals(1, drivers[1].number)
    }

    @Test
    fun `given a valid API response, when getDrivers is called, then mapper applies hash to team colour`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(DriverFixtures.allDriversJson())
        val repo = DriversRepositoryImpl(client)

        // When
        val result = repo.getDrivers(sessionKey = 9158)

        // Then
        assertIs<Result.Success<*>>(result)
        val drivers = (result as Result.Success).value
        assertEquals("#6CD3BF", drivers[0].teamColour)
        assertEquals("#3671C6", drivers[1].teamColour)
    }

    @Test
    fun `given an empty API response, when getDrivers is called, then returns empty list`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(DriverFixtures.emptyJson())
        val repo = DriversRepositoryImpl(client)

        // When
        val result = repo.getDrivers(sessionKey = 9158)

        // Then
        assertIs<Result.Success<*>>(result)
        assertEquals(0, (result as Result.Success).value.size)
    }

    @Test
    fun `given a 500 response, when getDrivers is called, then returns ServerError with code 500`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(
            responseJson = "Internal Server Error",
            status = HttpStatusCode.InternalServerError
        )
        val repo = DriversRepositoryImpl(client)

        // When
        val result = repo.getDrivers(sessionKey = 9158)

        // Then
        assertIs<Result.Failure<*>>(result)
        val error = (result as Result.Failure).error
        assertIs<DomainError.ServerError>(error)
        assertEquals(500, error.code)
    }

    @Test
    fun `given a 404 response, when getDrivers is called, then returns ServerError with code 404`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(
            responseJson = "Not Found",
            status = HttpStatusCode.NotFound
        )
        val repo = DriversRepositoryImpl(client)

        // When
        val result = repo.getDrivers(sessionKey = 9158)

        // Then
        assertIs<Result.Failure<*>>(result)
        val error = (result as Result.Failure).error
        assertIs<DomainError.ServerError>(error)
        assertEquals(404, error.code)
    }

    @Test
    fun `given malformed JSON, when getDrivers is called, then returns ParseError`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client("{ this is not valid json }")
        val repo = DriversRepositoryImpl(client)

        // When
        val result = repo.getDrivers(sessionKey = 9158)

        // Then
        assertIs<Result.Failure<*>>(result)
        assertIs<DomainError.ParseError>((result as Result.Failure).error)
    }
}
