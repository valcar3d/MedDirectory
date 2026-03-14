package com.example.meddirectory.di

import android.content.Context
import android.util.Log
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.addLastModifiedToFileCacheKey
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okio.Path.Companion.toOkioPath
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    private const val DISK_CACHE_SIZE_MB = 50
    private const val DISK_CACHE_SIZE_BYTES = DISK_CACHE_SIZE_MB * 1024L * 1024L
    private const val MEMORY_CACHE_PERCENT = 0.25
    private const val DISK_CACHE_DIRECTORY = "image_cache"

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve(DISK_CACHE_DIRECTORY).toOkioPath())
                    .maxSizeBytes(DISK_CACHE_SIZE_BYTES)
                    .build()
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, MEMORY_CACHE_PERCENT)
                    .build()
            }
            .crossfade(true)
            .addLastModifiedToFileCacheKey(true)
            .build()
    }
}
