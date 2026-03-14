package com.example.meddirectory.data.cache

import android.util.LruCache
import com.example.meddirectory.domain.model.FeedItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LruItemCache @Inject constructor() : ItemCache {
    private val cache = LruCache<String, FeedItem>(100)

    override fun get(key: String): FeedItem? = cache.get(key)

    override fun set(key: String, value: FeedItem) {
        cache.put(key, value)
    }

    override fun getAll(): List<FeedItem> = cache.snapshot().values.toList()

    override fun clear() = cache.evictAll()
}