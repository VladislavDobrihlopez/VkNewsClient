package com.voitov.vknewsclient.data.mappers

import android.util.Log
import com.voitov.vknewsclient.data.network.models.postsFeedModels.NewsFeedContentResponseDto
import com.voitov.vknewsclient.data.util.mapTimestampToDatePattern
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem
import javax.inject.Inject
import kotlin.math.absoluteValue

class PostMapper @Inject constructor() {
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
                    isLikedByUser = post.likes.userLikes == LIKED,
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

    companion object {
        private const val LIKED = 1
    }
}