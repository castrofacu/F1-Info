package com.f1.info.data.repository

import com.f1.info.domain.model.DomainError
import com.f1.info.domain.model.Result
import com.f1.info.test.fixtures.MockHttpClientFactory
import com.f1.info.test.fixtures.PositionFixtures
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PositionsRepositoryTest {

    @Test
    fun `given a valid API response, when getPositions is called, then returns mapped positions`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(PositionFixtures.allPositionsJson())
        val repo = PositionsRepositoryImpl(client)

        // When
        val result = repo.getPositions(sessionKey = 9158)

        // Then
        assertIs<Result.Success<*>>(result)
        val positions = (result as Result.Success).value
        assertEquals(2, positions.size)
        assertEquals(44, positions[0].driverNumber)
        assertEquals(1, positions[0].position)
        assertEquals(Instant.parse("2023-09-03T13:00:00Z"), positions[0].date)
    }

    @Test
    fun `given an empty API response, when getPositions is called, then returns empty list`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(PositionFixtures.emptyJson())
        val repo = PositionsRepositoryImpl(client)

        // When
        val result = repo.getPositions(sessionKey = 9158)

        // Then
        assertIs<Result.Success<*>>(result)
        assertEquals(0, (result as Result.Success).value.size)
    }

    @Test
    fun `given a 500 response, when getPositions is called, then returns ServerError with code 500`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(
            responseJson = "Internal Server Error",
            status = HttpStatusCode.InternalServerError
        )
        val repo = PositionsRepositoryImpl(client)

        // When
        val result = repo.getPositions(sessionKey = 9158)

        // Then
        assertIs<Result.Failure<*>>(result)
        val error = (result as Result.Failure).error
        assertIs<DomainError.ServerError>(error)
        assertEquals(500, error.code)
    }

    @Test
    fun `given a 404 response, when getPositions is called, then returns ServerError with code 404`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client(
            responseJson = "Not Found",
            status = HttpStatusCode.NotFound
        )
        val repo = PositionsRepositoryImpl(client)

        // When
        val result = repo.getPositions(sessionKey = 9158)

        // Then
        assertIs<Result.Failure<*>>(result)
        val error = (result as Result.Failure).error
        assertIs<DomainError.ServerError>(error)
        assertEquals(404, error.code)
    }

    @Test
    fun `given malformed JSON, when getPositions is called, then returns ParseError`() = runTest {
        // Given
        val client = MockHttpClientFactory.createOpenF1Client("{ this is not valid json }")
        val repo = PositionsRepositoryImpl(client)

        // When
        val result = repo.getPositions(sessionKey = 9158)

        // Then
        assertIs<Result.Failure<*>>(result)
        assertIs<DomainError.ParseError>((result as Result.Failure).error)
    }
}
