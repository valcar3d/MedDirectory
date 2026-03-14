package com.example.meddirectory.data.repository

import com.example.meddirectory.data.cache.ItemCache
import com.example.meddirectory.data.remote.FeedApiService
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.domain.repository.FeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepositoryImpl @Inject constructor(
    private val apiService: FeedApiService,
    private val itemCache: ItemCache
) : FeedRepository {

    private val _itemsFlow = MutableStateFlow<List<FeedItem>>(emptyList())
    override val itemsFlow: StateFlow<List<FeedItem>> = _itemsFlow.asStateFlow()

    override suspend fun getFeed(): Result<List<FeedItem>> {
        val result = runCatching {
            apiService.getFeed().map { it.toDomain() }
        }.toAppResult()

        result.getOrNull()?.let { items ->
            items.forEach { cacheItem(it) }
            _itemsFlow.value = items
        }

        return result
    }

    override suspend fun getItemByIdWithCache(id: String): Result<FeedItem> {
        itemCache[id]?.let { cachedItem ->
            return Result.success(cachedItem)
        }

        val result = fetchItemById(id)

        result.getOrNull()?.let { item ->
            cacheItem(item)
            _itemsFlow.value = itemCache.getAll()
        }

        return result
    }

    private suspend fun fetchItemById(id: String): Result<FeedItem> = runCatching {
        apiService.getFeed().map { it.toDomain() }
            .find { it.id == id }
            ?: throw NoSuchElementException("Item with id $id not found")
    }.toAppResult()

    private fun cacheItem(item: FeedItem) {
        itemCache[item.id] = item
    }
}