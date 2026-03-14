package com.example.meddirectory.domain.repository

import com.example.meddirectory.domain.model.FeedItem
import kotlinx.coroutines.flow.StateFlow

interface FeedRepository {
    suspend fun getFeed(): Result<List<FeedItem>>
    val itemsFlow: StateFlow<List<FeedItem>>
    suspend fun getItemByIdWithCache(id: String): Result<FeedItem>
}