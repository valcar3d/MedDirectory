package com.example.meddirectory.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meddirectory.common.AppError
import com.example.meddirectory.common.AppErrorException
import com.example.meddirectory.domain.usecases.GetItemByIdUseCase
import com.example.meddirectory.presentation.common.SalaryStats
import com.example.meddirectory.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = savedStateHandle["itemId"]
        ?: throw IllegalArgumentException("itemId is required")

    private val _uiState = MutableStateFlow<UiState<DetailData>>(UiState.Loading)
    val uiState: StateFlow<UiState<DetailData>> = _uiState.asStateFlow()

    init {
        loadItem()
    }

    fun retry() {
        loadItem()
    }

    private fun loadItem() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = getItemByIdUseCase(itemId)

            if (result.isSuccess) {
                val item = result.getOrThrow()
                val salaryStats = SalaryStats.fromItems(listOf(item))
                _uiState.value = UiState.Success(DetailData(item, salaryStats))
            } else {
                val error = result.exceptionOrNull()
                val appError = if (error is AppErrorException) error.error
                else AppError.UnknownError(error?.message)
                _uiState.value = UiState.Error(appError)
            }
        }
    }
}