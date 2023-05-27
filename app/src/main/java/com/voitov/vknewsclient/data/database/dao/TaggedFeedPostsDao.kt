package com.voitov.vknewsclient.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voitov.vknewsclient.data.database.models.TagDbModel
import com.voitov.vknewsclient.data.database.models.TaggedPostItemDbModel
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaggedFeedPostsDao {
    @Query("SELECT * FROM news_feed_articles")
    fun getAllNews(): Flow<List<TaggedPostItemDbModel>>
    @Query("SELECT * FROM news_feed_articles WHERE authorName=:itemTag")
    fun getNewsWithSpecifiedTag(itemTag: String): Flow<List<TaggedPostItemDbModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheNews(news: TaggedPostItemDbModel)
}