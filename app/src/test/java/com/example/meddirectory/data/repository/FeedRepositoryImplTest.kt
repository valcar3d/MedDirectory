package com.example.meddirectory.data.repository

import com.example.meddirectory.common.AppError
import com.example.meddirectory.common.AppErrorException
import com.example.meddirectory.data.remote.FeedApiService
import com.example.meddirectory.data.remote.dto.FeedItemDto
import com.example.meddirectory.data.remote.dto.LocationDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class FeedRepositoryImplTest {

    private lateinit var repository: FeedRepositoryImpl
    private val apiService: FeedApiService = mockk()

    @Before
    fun setup() {
        repository = FeedRepositoryImpl(apiService)
    }

    @Test
    fun `getFeed returns success when API call succeeds`() = runTest {
        // Given
        val dtoList = listOf(
            FeedItemDto(
                id = 1,
                firstName = "Duc Thinh",
                lastName = "Pham",
                suffix = "MD",
                specialty = "Cardiology",
                npi = "1942419106",
                location = LocationDto(city = "Houston", state = "TX"),
                salaryRange = "$480,000 - $620,000",
                acceptingNewPatients = true
            )
        )
        coEvery { apiService.getFeed() } returns dtoList

        // When
        val result = repository.getFeed()

        // Then
        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertEquals(1, items?.size)
        assertEquals("1", items?.get(0)?.id)
        assertEquals("Duc Thinh", items?.get(0)?.firstName)
        assertEquals("Pham", items?.get(0)?.lastName)
        assertEquals("MD", items?.get(0)?.suffix)
        assertEquals("Cardiology", items?.get(0)?.specialty)
        assertEquals("Houston, TX", items?.get(0)?.location)
    }

    @Test
    fun `getFeed returns error when IOException occurs`() = runTest {
        // Given
        coEvery { apiService.getFeed() } throws IOException("Network error")

        // When
        val result = repository.getFeed()

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is AppErrorException)
        assertTrue((exception as AppErrorException).error is AppError.NetworkError)
    }
}