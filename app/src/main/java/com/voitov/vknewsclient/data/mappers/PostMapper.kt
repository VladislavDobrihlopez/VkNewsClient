package com.voitov.vknewsclient.data.mappers

import android.util.Log
import com.voitov.vknewsclient.data.network.models.NewsFeedContentResponseDto
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class PostMapper {
    fun mapDtoResponseToEntitiesOfPostItem(response: NewsFeedContentResponseDto): List<PostItem> {
        val entities = mutableListOf<PostItem>()
        val posts = response.content.posts
        val communities = response.content.groups

        for (post in posts) {
            Log.d("PostMapper", post.toString())

            val postOwner = communities.find { community ->
                community.id == removeDifferenceBetweenCommunityAndUserPostOwner(post.communityId)
            } ?: break
            entities.add(
                PostItem(
                    id = post.id,
                    communityId = post.communityId,
                    communityPhotoUrl = postOwner.photoUrl,
                    authorName = postOwner.name,
                    contentText = post.text,
                    date = mapTimestampToDatePattern(post.secondsSince1970),
                    contentImageUrl = post.attachments?.firstOrNull()?.photo?.photos?.lastOrNull()?.url,
                    isLikedByUser = post.likes.userLikes == 1,
                    metrics = listOf(
                        SocialMetric(MetricsType.LIKES, post.likes.count),
                        SocialMetric(MetricsType.COMMENTS, post.comments.count),
                        SocialMetric(MetricsType.SHARES, post.reposts.count),
                        SocialMetric(MetricsType.VIEWS, post.views.count)
                    )
                )
            )
        }

        return entities
    }

    private fun removeDifferenceBetweenCommunityAndUserPostOwner(identifier: Long): Long {
        return identifier.absoluteValue
    }

    private fun mapTimestampToDatePattern(timestampInSeconds: Long): String {
        val date = Date(timestampInSeconds * 1000)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}