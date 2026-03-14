package com.example.meddirectory.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import com.example.meddirectory.common.AppError
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.common.SalaryStats
import com.example.meddirectory.presentation.screens.feed.FeedUiState

object PreviewData {
    
    val mockFeedItems = listOf(
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
        ),
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
        ),
        FeedItem(
            id = "3",
            firstName = "Fardad",
            lastName = "Esmailian",
            suffix = "MD",
            specialty = "Cardiothoracic Surgery",
            npi = "1740402486",
            location = "Los Angeles, CA",
            salaryRange = "$500,000 - $700,000",
            acceptingNewPatients = true
        )
    )
    
    val mockFeedItemHighSalary = mockFeedItems[0]
    val mockFeedItemLowSalary = mockFeedItems[1]
    val mockFeedItemNoSalary = FeedItem(
        id = "3",
        firstName = "Fardad",
        lastName = "Esmailian",
        suffix = "MD",
        specialty = "Cardiothoracic Surgery",
        npi = "1740402486",
        location = "Los Angeles, CA",
        salaryRange = null,
        acceptingNewPatients = true
    )
    
    val mockSalaryStats = SalaryStats(150000, 700000, 550000)
    
    val mockFeedUiStateLoading = FeedUiState.Loading
    
    val mockFeedUiStateSuccess = FeedUiState.Success(
        items = mockFeedItems,
        salaryStats = mockSalaryStats
    )
    
    val mockFeedUiStateError = FeedUiState.Error(AppError.NetworkError)

    @Composable
    fun mockImageLoader(): ImageLoader = ImageLoader.Builder(LocalContext.current).build()
}