package com.voitov.vknewsclient.data.mappers

import android.util.Log
import com.voitov.vknewsclient.data.network.models.postsFeedModels.GroupInfoDto
import com.voitov.vknewsclient.data.network.models.postsFeedModels.ProfileDto
import com.voitov.vknewsclient.data.network.models.profileModels.details.ProfileDataDto
import com.voitov.vknewsclient.data.network.models.profileModels.wall.InnerWallPostDto
import com.voitov.vknewsclient.data.network.models.profileModels.wall.ProfileWallContentDto
import com.voitov.vknewsclient.data.util.mapTimestampToDatePattern
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.InnerWallPost
import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost
import javax.inject.Inject
import kotlin.math.abs

class ProfileMapper @Inject constructor() {
    fun mapDtoToEntities(dto: ProfileDataDto): Profile {
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
            coverPhotoUrl = if (dto.cover?.enabled == ENABLED) dto.cover.images?.get(0)?.url else null
        )
    }

    fun mapDtoToEntities(dto: ProfileWallContentDto): List<WallPost> {
        val entities = mutableListOf<WallPost>()
        val posts = dto.posts

        for (post in posts) {
            val fromId = abs(post.fromId)
            val communityInfo = dto.communities.find {
                it.id == fromId
            }

            val name = communityInfo?.name ?: dto.profiles.find {
                it.id == fromId
            }?.let {
                "${it.firstName} ${it.lastName}"
            } ?: throw IllegalStateException()

            val url = communityInfo?.photoUrl ?: dto.profiles.find {
                it.id == fromId
            }?.avatarUrl ?: throw IllegalStateException()

            val entity = WallPost(
                id = post.id,
                producerId = fromId,
                producerPhotoUrl = url,
                producerName = name,
                postType = post.type,
                contentText = post.text,
                date = mapTimestampToDatePattern(post.secondsSince1970),
                contentImageUrl = post.attachments?.map { it.photo?.photos?.lastOrNull()?.url ?: "" }
                    ?: listOf(),
                isLikedByUser = post.likes.userLikes == LIKED,
                isSharedByUser = post.reposts.userShares == SHARED,
                metrics = listOf(
                    SocialMetric(MetricsType.LIKES, post.likes.count),
                    SocialMetric(MetricsType.COMMENTS, post.comments.count),
                    SocialMetric(MetricsType.SHARES, post.reposts.count),
                    SocialMetric(MetricsType.VIEWS, post.views?.count ?: 0)
                ),
                postLifecycleHierarchy =
                post.copyHistory?.map {
                    mapDtoEntity(it, dto.communities, dto.profiles)
                } ?: listOf()
            )

            Log.d("PostMapper", entity.toString())
            entities.add(entity)
        }
        return entities
    }

    private fun mapDtoEntity(
        post: InnerWallPostDto,
        communities: List<GroupInfoDto>,
        profiles: List<ProfileDto>
    ): InnerWallPost {
        val fromId = abs(post.fromId)
        val communityInfo = communities.find {
            it.id == fromId
        }

        val name = communityInfo?.name ?: profiles.find {
            it.id == fromId
        }?.let {
            "${it.firstName} ${it.lastName}"
        } ?: throw IllegalStateException()

        val url = communityInfo?.photoUrl ?: profiles.find {
            it.id == fromId
        }?.avatarUrl ?: throw IllegalStateException()

        return InnerWallPost(
            id = post.id,
            producerId = post.fromId,
            producerPhotoUrl = url,
            producerName = name,
            postType = post.type,
            contentText = post.text,
            date = mapTimestampToDatePattern(post.secondsSince1970),
            contentImageUrl = post.attachments?.map { it.photo?.photos?.lastOrNull()?.url ?: "" }
                ?: listOf()//post.attachments?.firstOrNull()?.photo?.photos?.lastOrNull()?.url,
        )
    }

    companion object {
        private const val ENABLED = 1
        private const val LIKED = 1
        private const val SHARED = 1
    }
}