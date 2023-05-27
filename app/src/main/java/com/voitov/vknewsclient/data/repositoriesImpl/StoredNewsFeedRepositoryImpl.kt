package com.voitov.vknewsclient.data.repositoriesImpl

import com.voitov.vknewsclient.data.database.dao.TaggedFeedPostsDao
import com.voitov.vknewsclient.data.database.dao.TagsDao
import com.voitov.vknewsclient.data.mappers.TaggedPostMapper
import com.voitov.vknewsclient.data.mappers.TagsMapper
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoredNewsFeedRepositoryImpl @Inject constructor(
    private val tagsDao: TagsDao,
    private val taggedFeedPostsDao: TaggedFeedPostsDao,
    private val tagsMapper: TagsMapper,
    private val taggedPostMapper: TaggedPostMapper
) : StoredNewsFeedRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            tagsDao.saveTag(tagsMapper.mapEntityToDbModel(ItemTag("programming")))
            tagsDao.saveTag(tagsMapper.mapEntityToDbModel(ItemTag("read later")))
        }
    }

    override fun getAllNews(): Flow<List<TaggedPostItem>> {
        TODO("Not yet implemented")
    }

    override fun getNewsWithSpecifiedTag(itemTag: ItemTag): Flow<List<TaggedPostItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun cacheNews(news: TaggedPostItem) {
        taggedFeedPostsDao.cacheNews(taggedPostMapper.mapEntityToDbModel(news))
    }

    override fun getAllTags(): Flow<List<ItemTag>> {
        return tagsDao.getAllTags().map { dbModels ->
            dbModels.map { tagsMapper.mapDbModelToEntity(it) }
        }
    }

    override suspend fun saveTag(tag: ItemTag) {
        tagsDao.saveTag(tagsMapper.mapEntityToDbModel(tag))
    }

    override suspend fun removeTag(tag: ItemTag) {
        //tagsDao.removeTag(tag.name)
        TODO("Not yet implemented")
    }
}