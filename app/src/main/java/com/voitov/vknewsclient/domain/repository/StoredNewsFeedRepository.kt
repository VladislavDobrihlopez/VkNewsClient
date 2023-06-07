package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StoredNewsFeedRepository {
    fun getAllPosts(): StateFlow<List<TaggedPostItem>>
    suspend fun getPostsWithSpecifiedTags(itemTags: List<ItemTag>)
    suspend fun cachePosts(post: TaggedPostItem)
    suspend fun removeCachedPost(postId: Long)
    fun getAllTags(): Flow<List<ItemTag>>
    suspend fun saveTag(tag: ItemTag)
    suspend fun removeTag(tag: ItemTag)
}