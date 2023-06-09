package com.voitov.vknewsclient.data.mappers

import android.util.Log
import com.voitov.vknewsclient.data.network.models.profileModels.ProfileDataDto
import com.voitov.vknewsclient.data.network.models.profileModels.ProfileWallContentDto
import com.voitov.vknewsclient.data.util.mapTimestampToDatePattern
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost
import javax.inject.Inject

class ProfileMapper @Inject constructor() {
    fun mapDtoToEntity(dto: ProfileDataDto): Profile {
        return Profile(
            shortenedLink = dto.shortenedLink,
            firstName = dto.firstName,
            lastName = dto.lastName,
            deactivated = dto?.deactivated, // do not change
            isClosed = dto.isClosed,
            canAccessClosed = dto.canAccessClosed,
            birthday = dto?.birthday,
            photoMaxUrl = dto.photoMaxUrl,
            about = dto?.about ?: "",
            followersCount = dto.followersCount,
            isOnline = dto.isOnline,
            cityName = dto.city?.name,
            countryName = dto.country?.title,
            coverPhotoUrl = if (dto.cover.enabled == ENABLED) dto.cover.images[0].url else null
        )
    }

    fun mapDtoToEntity(dto: ProfileWallContentDto): List<WallPost> {
        val entities = mutableListOf<WallPost>()
        val posts = dto.posts

        for (post in posts) {
            Log.d("PostMapper", post.toString())

            entities.add(
                WallPost(
                    id = post.id,
                    contentText = post.text,
                    date = mapTimestampToDatePattern(post.secondsSince1970),
                    contentImageUrl = post.attachments?.firstOrNull()?.photo?.photos?.lastOrNull()?.url,
                    isLikedByUser = post.likes.userLikes == LIKED,
                    metrics = listOf(
                        SocialMetric(MetricsType.LIKES, post.likes.count),
                        SocialMetric(MetricsType.COMMENTS, post.comments.count),
                        SocialMetric(MetricsType.SHARES, post.reposts.count),
                        SocialMetric(MetricsType.VIEWS, post.views.count)
                    ),
                )
            )
        }
        return entities
    }

    companion object {
        private const val ENABLED = 1
        private const val LIKED = 1
    }
}