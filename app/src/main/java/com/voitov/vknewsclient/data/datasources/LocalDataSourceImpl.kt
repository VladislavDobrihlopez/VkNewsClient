package com.voitov.vknewsclient.data.datasources

import android.util.Log
import com.voitov.vknewsclient.data.database.dao.TaggedFeedPostsDao
import com.voitov.vknewsclient.data.database.dao.TagsDao
import com.voitov.vknewsclient.data.mappers.TaggedPostMapper
import com.voitov.vknewsclient.data.mappers.TagsMapper
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val tagsDao: TagsDao,
    private val taggedFeedPostsDao: TaggedFeedPostsDao,
    private val tagsMapper: TagsMapper,
    private val taggedPostMapper: TaggedPostMapper
) : LocalDataSource {
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            val filters = tagsDao.getAllTags().map { dbModel ->
                tagsMapper.mapDbModelToEntity(dbModel)
            }
            tagsForPostFiltering = filters
        }
    }

    private var neededToBeLoaded = true
    private var cachedPosts = mutableListOf<TaggedPostItem>()
    private val shouldLoadDataEvent = MutableSharedFlow<Any>()

    private var tagsForPostFiltering: List<ItemTag>? = null
    private val shouldLoadAvailableTags = MutableSharedFlow<Any>()

    private val updatedPostsFlow = MutableSharedFlow<List<TaggedPostItem>>()

    private val allPostsFlow: StateFlow<List<TaggedPostItem>> = flow {
        if (neededToBeLoaded) {
            taggedFeedPostsDao.getAllNews().map { dbModel ->
                val post = taggedPostMapper.mapDbModelToEntity(dbModel)
                cachedPosts.add(post)
                post
            }
            neededToBeLoaded = false
        }
        emit(cachedPosts)

        shouldLoadDataEvent.collect {
            if (neededToBeLoaded) {
                cachedPosts = mutableListOf()
                taggedFeedPostsDao.getAllNews().map { dbModel ->
                    val post = taggedPostMapper.mapDbModelToEntity(dbModel)
                    cachedPosts.add(post)
                    post
                }
                neededToBeLoaded = false
            }
            emit(cachedPosts)
        }
    }
        .map { taggedPosts ->
            if (tagsForPostFiltering == null) {
                taggedPosts
            } else {
                taggedPosts.filter { post ->
                    tagsForPostFiltering?.contains(post.tag) ?: throw IllegalStateException()
                }
            }
        }
        .stateIn(scope, started = SharingStarted.Lazily, initialValue = listOf())

    override fun getAllPosts(): StateFlow<List<TaggedPostItem>> {
        return allPostsFlow
    }

    override suspend fun getPostsWithSpecifiedTags(itemTags: List<ItemTag>) {
        tagsForPostFiltering = itemTags.toList()
        shouldLoadDataEvent.emit(Any())
        //updatedPostsFlow.emit(cachedPosts)
    }

    override suspend fun cachePosts(post: TaggedPostItem) {
        neededToBeLoaded = true
        taggedFeedPostsDao.cacheNews(taggedPostMapper.mapEntityToDbModel(post))
        shouldLoadDataEvent.emit(Unit)
    }

    override suspend fun removeCachedPost(postId: Long) {
        taggedFeedPostsDao.removeCachedPost(postId)
        cachedPosts.removeAll {
            it.postItem.id == postId
        }
        shouldLoadDataEvent.emit(Any())
    }

    private val allTagsFlow = flow<List<ItemTag>> {
        emit(tagsDao.getAllTags().map { dbModel ->
            tagsMapper.mapDbModelToEntity(dbModel)
        })
        shouldLoadAvailableTags.collect {
            emit(tagsDao.getAllTags().map { dbModel ->
                tagsMapper.mapDbModelToEntity(dbModel)
            })
        }
    }.onEach { tagsForPostFiltering = it.toList() }
        .shareIn(scope, SharingStarted.Lazily, replay = 1)

    override fun getAllTags(): Flow<List<ItemTag>> {
        return allTagsFlow
    }

    override suspend fun saveTag(tag: ItemTag) {
        tagsDao.saveTag(tagsMapper.mapEntityToDbModel(tag))
        shouldLoadAvailableTags.emit(Any())
    }

    override suspend fun removeTag(tag: ItemTag) {
        tagsDao.removeTag(tag.name)
        shouldLoadAvailableTags.emit(Any())
        neededToBeLoaded = true
        shouldLoadDataEvent.emit(Unit)
    }
}