package com.example.meddirectory.presentation.screens.feed

import com.example.meddirectory.common.AppError
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.common.SalaryStats

sealed class FeedUiState {
    data object Loading : FeedUiState()
    data class Success(
        val items: List<FeedItem>,
        val salaryStats: SalaryStats? = null
    ) : FeedUiState()
    data class Error(val error: AppError) : FeedUiState()
}
