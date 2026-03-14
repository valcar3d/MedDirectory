package com.example.meddirectory.presentation.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import com.example.meddirectory.R
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.PreviewData
import com.example.meddirectory.presentation.common.UiState
import com.example.meddirectory.presentation.common.components.ErrorContent
import com.example.meddirectory.presentation.screens.feed.components.FeedItemCard
import com.example.meddirectory.ui.theme.MedDirectoryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onItemClick: (String) -> Unit,
    imageLoader: ImageLoader,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.feed_title))
                },
                actions = {
                    IconButton(onClick = { viewModel.retry() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        FeedContent(
            uiState = uiState,
            imageLoader = imageLoader,
            onItemClick = onItemClick,
            onRetry = { viewModel.retry() },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun FeedContent(
    uiState: UiState<FeedData>,
    imageLoader: ImageLoader,
    onItemClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is UiState.Error -> {
                ErrorContent(
                    error = uiState.error,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is UiState.Success -> {
                FeedList(
                    items = uiState.data.items,
                    salaryStats = uiState.data.salaryStats,
                    imageLoader = imageLoader,
                    onItemClick = { item ->
                        onItemClick(item.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun FeedList(
    items: List<FeedItem>,
    salaryStats: com.example.meddirectory.presentation.common.SalaryStats?,
    imageLoader: ImageLoader,
    onItemClick: (FeedItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            FeedItemCard(
                item = item,
                salaryStats = salaryStats,
                imageLoader = imageLoader,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenLoadingPreview() {
    MedDirectoryTheme {
        FeedContent(
            uiState = PreviewData.mockFeedUiStateLoading,
            imageLoader = PreviewData.mockImageLoader(),
            onItemClick = { },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenSuccessPreview() {
    MedDirectoryTheme {
        FeedContent(
            uiState = PreviewData.mockFeedUiStateSuccess,
            imageLoader = PreviewData.mockImageLoader(),
            onItemClick = { },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenErrorPreview() {
    MedDirectoryTheme {
        FeedContent(
            uiState = PreviewData.mockFeedUiStateError,
            imageLoader = PreviewData.mockImageLoader(),
            onItemClick = { },
            onRetry = {}
        )
    }
}