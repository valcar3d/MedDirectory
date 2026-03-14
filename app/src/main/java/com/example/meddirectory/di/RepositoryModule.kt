package com.example.meddirectory.di

import com.example.meddirectory.data.cache.ItemCache
import com.example.meddirectory.data.cache.LruItemCache
import com.example.meddirectory.data.repository.FeedRepositoryImpl
import com.example.meddirectory.domain.repository.FeedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsFeedRepository(
        impl: FeedRepositoryImpl
    ): FeedRepository

    @Binds
    @Singleton
    abstract fun bindsItemCache(
        impl: LruItemCache
    ): ItemCache
}
