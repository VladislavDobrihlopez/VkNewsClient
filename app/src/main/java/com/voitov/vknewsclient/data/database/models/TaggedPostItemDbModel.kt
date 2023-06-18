package com.voitov.vknewsclient.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.voitov.vknewsclient.domain.SocialMetric

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
    @PrimaryKey
    val id: Long,
    val tag: String,
    val communityId: Long,
    val communityPhotoUrl: String,
    val authorName: String,
    val dateInMillis: Long,
    val contentText: String,
    val isLikedByUser: Boolean,
    val isSharedByUser: Boolean,
    val contentImageUrl: List<String>,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val views: Int
)