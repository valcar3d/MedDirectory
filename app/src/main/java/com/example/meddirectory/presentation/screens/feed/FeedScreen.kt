package com.example.meddirectory.presentation.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import com.example.meddirectory.R
import com.example.meddirectory.common.AppError
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.PreviewData
import com.example.meddirectory.presentation.common.SalaryStats
import com.example.meddirectory.presentation.screens.feed.components.FeedItemCard
import com.example.meddirectory.ui.theme.MedDirectoryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onItemClick: (FeedItem, SalaryStats?) -> Unit,
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
    uiState: FeedUiState,
    imageLoader: ImageLoader,
    onItemClick: (FeedItem, SalaryStats?) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState) {
            is FeedUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is FeedUiState.Error -> {
                ErrorContent(
                    error = uiState.error,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is FeedUiState.Success -> {
                FeedList(
                    items = uiState.items,
                    salaryStats = uiState.salaryStats,
                    imageLoader = imageLoader,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun FeedList(
    items: List<FeedItem>,
    salaryStats: SalaryStats?,
    imageLoader: ImageLoader,
    onItemClick: (FeedItem, SalaryStats?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            FeedItemCard(
                item = item,
                salaryStats = salaryStats,
                imageLoader = imageLoader,
                onClick = { onItemClick(item, salaryStats) }
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = when (error) {
                is AppError.NetworkError -> stringResource(R.string.error_network)
                is AppError.NotFoundError -> stringResource(R.string.error_not_found)
                is AppError.ServerError -> stringResource(R.string.error_server, error.message ?: error.code.toString())
                is AppError.UnknownError -> stringResource(R.string.error_unknown)
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
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
            onItemClick = { _, _ -> },
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
            onItemClick = { _, _ -> },
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
            onItemClick = { _, _ -> },
            onRetry = {}
        )
    }
}
