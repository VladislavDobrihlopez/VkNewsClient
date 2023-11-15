package com.voitov.vknewsclient.data.repositoriesImpl

import com.voitov.vknewsclient.data.datasources.LocalDataSource
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import javax.inject.Inject

class StoredPostsFeedRepositoryImpl @Inject constructor(
    private val source: LocalDataSource
) : StoredPostsFeedRepository {
    override fun getAllPosts() = source.getAllPosts()
    override fun getAllTags() = source.getAllTags()

    override suspend fun getPostsWithSpecifiedTags(itemTags: List<ItemTag>) {
        source.getPostsWithSpecifiedTags(itemTags)
    }

    override suspend fun cachePosts(post: TaggedPostItem) {
        source.cachePosts(post)
    }

    override suspend fun removeCachedPost(postId: Long) {
        source.removeCachedPost(postId)
    }

    override suspend fun saveTag(tag: ItemTag) {
        source.saveTag(tag)
    }

    override suspend fun removeTag(tag: ItemTag) {
        source.removeTag(tag)
    }
}