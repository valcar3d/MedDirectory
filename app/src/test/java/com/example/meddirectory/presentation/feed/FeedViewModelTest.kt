package com.example.meddirectory.presentation.feed

import app.cash.turbine.test
import com.example.meddirectory.common.AppError
import com.example.meddirectory.common.AppErrorException
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.domain.usecases.GetFeedUseCase
import com.example.meddirectory.presentation.common.UiState
import com.example.meddirectory.presentation.screens.feed.FeedData
import com.example.meddirectory.presentation.screens.feed.FeedViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: FeedViewModel
    private val getFeedUseCase: GetFeedUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadFeed emits loading then success`() = runTest {
        // Given
        val items = listOf(
            FeedItem(
                id = "1",
                firstName = "Duc Thinh",
                lastName = "Pham",
                suffix = "MD",
                specialty = "Cardiology",
                npi = "1942419106",
                location = "Houston, TX",
                salaryRange = "$480,000 - $620,000",
                acceptingNewPatients = true
            )
        )
        coEvery { getFeedUseCase() } returns Result.success(items)

        // When
        viewModel = FeedViewModel(getFeedUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assert(state is UiState.Success)
            assertEquals(items, (state as UiState.Success).data.items)
            assertNotNull(state.data.salaryStats)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadFeed emits error when use case fails`() = runTest {
        // Given
        val appError = AppError.NetworkError
        coEvery { getFeedUseCase() } returns Result.failure(AppErrorException(appError))

        // When
        viewModel = FeedViewModel(getFeedUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Error(appError), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry reloads feed`() = runTest {
        // Given
        val items = listOf(
            FeedItem(
                id = "2",
                firstName = "Patricia",
                lastName = "Tavares",
                suffix = "DC",
                specialty = "Chiropractic",
                npi = "1053387928",
                location = "Boise, ID",
                salaryRange = "$150,000 - $200,000",
                acceptingNewPatients = false
            )
        )
        coEvery { getFeedUseCase() } returns Result.success(items)

        // When
        viewModel = FeedViewModel(getFeedUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assert(state is UiState.Success)
            assertEquals(items, (state as UiState.Success<FeedData>).data.items)
            cancelAndIgnoreRemainingEvents()
        }
    }
}