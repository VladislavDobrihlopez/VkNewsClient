package com.voitov.vknewsclient.domain.entities

data class PostCommentItem(
    val id: Long,
    val authorId: Long,
    val postId: Long,
    val authorName: String,
    val authorLastName: String,
    val avatarImageUrl: String,
    val date: String,
    val text: String,
    val likesCount: Int,
    val isLikedByUser: Boolean
)