package com.example.meddirectory.presentation.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meddirectory.common.AppError
import com.example.meddirectory.common.AppErrorException
import com.example.meddirectory.domain.usecases.GetFeedUseCase
import com.example.meddirectory.presentation.common.SalaryStats
import com.example.meddirectory.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedUseCase: GetFeedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<FeedData>>(UiState.Loading)
    val uiState: StateFlow<UiState<FeedData>> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = getFeedUseCase()

            _uiState.value = if (result.isSuccess) {
                val items = result.getOrDefault(emptyList())
                val salaryStats = SalaryStats.fromItems(items)
                UiState.Success(FeedData(items, salaryStats))
            } else {
                val error = result.exceptionOrNull()
                val appError = if (error is AppErrorException) error.error
                else AppError.UnknownError(error?.message)
                UiState.Error(appError)
            }
        }
    }

    fun retry() {
        loadFeed()
    }
}