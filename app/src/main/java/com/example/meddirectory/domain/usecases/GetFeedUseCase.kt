package com.example.meddirectory.domain.usecases

import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.domain.repository.FeedRepository
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(): Result<List<FeedItem>> = repository.getFeed()
}