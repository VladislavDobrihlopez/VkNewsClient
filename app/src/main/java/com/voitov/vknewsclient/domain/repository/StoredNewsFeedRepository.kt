package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import kotlinx.coroutines.flow.Flow

interface StoredNewsFeedRepository {
    fun getAllNews(): Flow<List<TaggedPostItem>>
    fun getNewsWithSpecifiedTag(itemTag: ItemTag): Flow<List<TaggedPostItem>>
    suspend fun cacheNews(news: TaggedPostItem)
    fun getAllTags(): Flow<List<ItemTag>>
    suspend fun saveTag(tag: ItemTag)
    suspend fun removeTag(tag: ItemTag)
}