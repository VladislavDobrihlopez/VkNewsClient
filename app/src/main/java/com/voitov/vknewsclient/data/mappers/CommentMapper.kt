package com.voitov.vknewsclient.data.mappers

import com.voitov.vknewsclient.data.network.models.CommentsContentResponseDto
import com.voitov.vknewsclient.data.util.mapTimestampToDatePattern
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import javax.inject.Inject

class CommentMapper @Inject constructor() {
    fun mapDtoToEntity(response: CommentsContentResponseDto): List<PostCommentItem> {
        val entities = mutableListOf<PostCommentItem>()
        val items = response.content.items
        val profiles = response.content.profiles

        for (item in items) {
            val profile = profiles.firstOrNull { it.id == item.fromId } ?: continue
            val entity = PostCommentItem(
                id = item.id,
                authorId = profile.id,
                authorName = profile.firstName,
                postId = item.id,
                authorLastName = profile.lastName,
                avatarImageUrl = profile.avatarUrl,
                date = mapTimestampToDatePattern(item.date),
                text = item.text,
                likesCount = item.likeInfo.count,
                isLikedByUser = item.likeInfo.isLikedByUser == LIKED
            )

            entities.add(entity)
        }
        return entities
    }

    companion object {
        private const val LIKED = 1
    }
}