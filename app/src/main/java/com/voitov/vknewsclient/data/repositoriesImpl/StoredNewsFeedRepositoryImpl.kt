package com.voitov.vknewsclient.data.repositoriesImpl

import android.util.Log
import com.voitov.vknewsclient.data.database.dao.TaggedFeedPostsDao
import com.voitov.vknewsclient.data.database.dao.TagsDao
import com.voitov.vknewsclient.data.database.models.TagDbModel
import com.voitov.vknewsclient.data.mappers.TaggedPostMapper
import com.voitov.vknewsclient.data.mappers.TagsMapper
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoredNewsFeedRepositoryImpl @Inject constructor(
    private val tagsDao: TagsDao,
    private val taggedFeedPostsDao: TaggedFeedPostsDao,
    private val tagsMapper: TagsMapper,
    private val taggedPostMapper: TaggedPostMapper
) : StoredNewsFeedRepository {
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
//            tagsDao.saveTag(TagDbModel("read later"))
//            tagsDao.saveTag(TagDbModel("this weekend"))

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
        .onCompletion { Log.d("TEST_POSTS_FLOW", "[repository] completion allPostsFlow") }
        //.mergeWith(updatedPostsFlow)
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

    private val allTagsFlow = flow<List<ItemTag>> {
        emit(tagsDao.getAllTags().map { dbModel ->
            Log.d("TEST_POSTS_FLOW", "[repository] emit tags")
            tagsMapper.mapDbModelToEntity(dbModel)
        })
        shouldLoadAvailableTags.collect {
            emit(tagsDao.getAllTags().map { dbModel ->
                Log.d("TEST_POSTS_FLOW", "[repository] emit tags")
                tagsMapper.mapDbModelToEntity(dbModel)
            })
        }
    }.onCompletion { Log.d("TEST_POSTS_FLOW", "[repository] onCompletion") }
        .shareIn(scope, SharingStarted.Lazily, replay = 1)

    override fun getAllTags(): Flow<List<ItemTag>> {
        return allTagsFlow
    }

    override suspend fun saveTag(tag: ItemTag) {
        tagsDao.saveTag(tagsMapper.mapEntityToDbModel(tag))
    }

    override suspend fun removeTag(tag: ItemTag) {
        //tagsDao.removeTag(tag.name)
        TODO("Not yet implemented")
    }
}