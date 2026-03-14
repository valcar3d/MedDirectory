package com.example.meddirectory.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.meddirectory.common.AppError
import com.example.meddirectory.common.AppErrorException
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.domain.usecases.GetItemByIdUseCase
import com.example.meddirectory.presentation.common.UiState
import com.example.meddirectory.presentation.screens.detail.DetailViewModel
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DetailViewModel
    private val getItemByIdUseCase: GetItemByIdUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadItem emits loading then success`() = runTest {
        // Given
        val item = FeedItem(
            id = "1",
            firstName = "John",
            lastName = "Doe",
            suffix = "MD",
            specialty = "Cardiology",
            npi = "1234567890",
            location = "New York, NY",
            salaryRange = "$100,000 - $200,000",
            acceptingNewPatients = true
        )
        coEvery { getItemByIdUseCase("1") } returns Result.success(item)

        val savedStateHandle = SavedStateHandle().apply {
            set("itemId", "1")
        }

        // When
        viewModel = DetailViewModel(getItemByIdUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(item, (state as UiState.Success).data.item)
    }

    @Test
    fun `loadItem emits error when use case fails`() = runTest {
        // Given
        val appError = AppError.NotFoundError
        coEvery { getItemByIdUseCase("1") } returns Result.failure(AppErrorException(appError))

        val savedStateHandle = SavedStateHandle().apply {
            set("itemId", "1")
        }

        // When
        viewModel = DetailViewModel(getItemByIdUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals(appError, (state as UiState.Error).error)
    }

    @Test
    fun `retry reloads item`() = runTest {
        // Given
        val item = FeedItem(
            id = "1",
            firstName = "Jane",
            lastName = "Smith",
            suffix = "DO",
            specialty = "Dermatology",
            npi = "0987654321",
            location = "Los Angeles, CA",
            salaryRange = "$150,000 - $250,000",
            acceptingNewPatients = false
        )
        coEvery { getItemByIdUseCase("1") } returns Result.success(item)

        val savedStateHandle = SavedStateHandle().apply {
            set("itemId", "1")
        }

        // When
        viewModel = DetailViewModel(getItemByIdUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(item, (state as UiState.Success).data.item)
    }
}