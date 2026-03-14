package com.example.meddirectory.data.remote

import com.example.meddirectory.data.constants.ApiConstants.FEED_ENDPOINT
import com.example.meddirectory.data.remote.dto.FeedItemDto
import retrofit2.http.GET

interface FeedApiService {
    @GET(value = FEED_ENDPOINT)
    suspend fun getFeed(): List<FeedItemDto>
}
