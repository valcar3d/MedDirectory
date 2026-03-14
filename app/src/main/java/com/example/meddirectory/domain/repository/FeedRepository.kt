package com.example.meddirectory.domain.repository

import com.example.meddirectory.domain.model.FeedItem

interface FeedRepository {
    suspend fun getFeed(): Result<List<FeedItem>>
}
