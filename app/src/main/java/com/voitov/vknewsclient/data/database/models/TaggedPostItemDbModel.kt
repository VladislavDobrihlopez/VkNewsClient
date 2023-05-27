package com.voitov.vknewsclient.data.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.voitov.vknewsclient.domain.entities.PostItem

@Entity(
    tableName = "news_feed_articles",
    foreignKeys = [
        ForeignKey(
            entity = TagDbModel::class,
            parentColumns = ["name"],
            childColumns = ["tag"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaggedPostItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tag: String,
    val communityId: Long,
    val authorName: String,
    val date: String,
    val contentText: String,
)