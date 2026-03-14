package com.example.meddirectory.data.cache

import com.example.meddirectory.domain.model.FeedItem

interface ItemCache {
    operator fun get(key: String): FeedItem?
    operator fun set(key: String, value: FeedItem)
    fun getAll(): List<FeedItem>
    fun clear()
}