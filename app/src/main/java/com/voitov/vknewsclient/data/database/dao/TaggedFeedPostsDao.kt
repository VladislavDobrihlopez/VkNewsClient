package com.voitov.vknewsclient.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voitov.vknewsclient.data.database.models.TaggedPostItemDbModel

@Dao
interface TaggedFeedPostsDao {
    @Query("SELECT * FROM news_feed_articles")
    suspend fun getAllNews(): List<TaggedPostItemDbModel>

    //@Query("SELECT * FROM news_feed_articles WHERE authorName=:itemTag")
    //fun getNewsWithSpecifiedTag(itemTag: String): Flow<List<TaggedPostItemDbModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheNews(news: TaggedPostItemDbModel)

    @Query("DELETE FROM news_feed_articles WHERE id=:postId")
    suspend fun removeCachedPost(postId: Long)
}