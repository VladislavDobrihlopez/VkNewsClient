package com.voitov.vknewsclient.data.mappers

import android.util.Log
import com.voitov.vknewsclient.data.database.models.TaggedPostItemDbModel
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import javax.inject.Inject

class TaggedPostMapper @Inject constructor() {
    fun mapEntityToDbModel(entity: TaggedPostItem): TaggedPostItemDbModel {
        Log.d("TEST_MAPPER", entity.tag.name)
        return TaggedPostItemDbModel(
            tag = entity.tag.name,
            id = entity.postItem.id,
            communityId = entity.postItem.communityId,
            communityPhotoUrl = entity.postItem.communityPhotoUrl,
            authorName = entity.postItem.authorName,
            date = entity.postItem.date,
            contentText = entity.postItem.contentText,
            isLikedByUser = entity.postItem.isLikedByUser,
            contentImageUrl = entity.postItem.contentImageUrl,
            likes = entity.postItem.metrics.find { it.type == MetricsType.LIKES }?.count
                ?: METRICS_BY_DEFAULT,
            comments = entity.postItem.metrics.find { it.type == MetricsType.COMMENTS }?.count
                ?: METRICS_BY_DEFAULT,
            shares = entity.postItem.metrics.find { it.type == MetricsType.SHARES }?.count
                ?: METRICS_BY_DEFAULT,
            views = entity.postItem.metrics.find { it.type == MetricsType.VIEWS }?.count
                ?: METRICS_BY_DEFAULT,
        )
    }

    fun mapDbModelToEntity(dbModel: TaggedPostItemDbModel): TaggedPostItem {
        Log.d("TEST_MAPPER", dbModel.tag)
        return TaggedPostItem(
            tag = ItemTag(dbModel.tag),
            postItem = PostItem(
                id = dbModel.id,
                communityId = dbModel.communityId,
                communityPhotoUrl = dbModel.communityPhotoUrl,
                authorName = dbModel.authorName,
                date = dbModel.date,
                contentText = dbModel.contentText,
                isLikedByUser = dbModel.isLikedByUser,
                contentImageUrl = dbModel.contentImageUrl,
                metrics = listOf(
                    SocialMetric(MetricsType.LIKES, dbModel.likes),
                    SocialMetric(MetricsType.COMMENTS, dbModel.comments),
                    SocialMetric(MetricsType.SHARES, dbModel.shares),
                    SocialMetric(MetricsType.VIEWS, dbModel.views)
                )
            )
        )
    }

    companion object {
        private const val METRICS_BY_DEFAULT = 0
    }
}