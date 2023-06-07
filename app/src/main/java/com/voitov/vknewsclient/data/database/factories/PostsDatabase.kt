package com.voitov.vknewsclient.data.database.factories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.voitov.vknewsclient.data.database.dao.TaggedFeedPostsDao
import com.voitov.vknewsclient.data.database.dao.TagsDao
import com.voitov.vknewsclient.data.database.models.TagDbModel
import com.voitov.vknewsclient.data.database.models.TaggedPostItemDbModel

@Database(
    entities = [TaggedPostItemDbModel::class, TagDbModel::class],
    version = 4,
    exportSchema = false
)
abstract class PostsDatabase() : RoomDatabase() {
    companion object {
        private const val DB_NAME = "cached_posts.db"
        private var instance: PostsDatabase? = null
        private val MONITOR = Any()
        fun getInstance(context: Context): PostsDatabase {
            synchronized(MONITOR) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(context, PostsDatabase::class.java, DB_NAME)
                    .allowMainThreadQueries().build()
                instance = db
                return db
            }
        }
    }

    abstract fun getTaggedPostsDao(): TaggedFeedPostsDao
    abstract fun getTagsDao(): TagsDao
}