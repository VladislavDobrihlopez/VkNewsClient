package com.voitov.vknewsclient.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voitov.vknewsclient.data.database.models.TagDbModel
import com.voitov.vknewsclient.data.database.models.TaggedPostItemDbModel
import com.voitov.vknewsclient.domain.entities.ItemTag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Query("SELECT * FROM feed_tags")
    fun getAllTags(): Flow<List<TagDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTag(tag: TagDbModel)

    @Query("DELETE FROM feed_tags WHERE name=:tag")
    suspend fun removeTag(tag: String)
}