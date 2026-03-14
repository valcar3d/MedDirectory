package com.example.meddirectory.data.cache

import com.example.meddirectory.domain.model.FeedItem

class TestItemCache : ItemCache {
    private val cache = mutableMapOf<String, FeedItem>()

    override fun get(key: String): FeedItem? = cache[key]

    override fun set(key: String, value: FeedItem) {
        cache[key] = value
    }

    override fun getAll(): List<FeedItem> = cache.values.toList()

    override fun clear() = cache.clear()
}