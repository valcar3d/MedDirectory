package com.example.meddirectory.data.repository

import com.example.meddirectory.data.remote.FeedApiService
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.domain.repository.FeedRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepositoryImpl @Inject constructor(
    private val apiService: FeedApiService
) : FeedRepository {

    override suspend fun getFeed(): Result<List<FeedItem>> = runCatching {
        apiService.getFeed().map { it.toDomain() }
    }.toAppResult()
}