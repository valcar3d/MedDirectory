package com.example.meddirectory.domain.usecases

import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.domain.repository.FeedRepository
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(id: String): Result<FeedItem> {
        return feedRepository.getItemByIdWithCache(id)
    }
}