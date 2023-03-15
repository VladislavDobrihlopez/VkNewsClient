package com.voitov.vknewsclient.domain.entities

import com.voitov.vknewsclient.R

data class PostCommentItem(
    val id: Int,
    val authorId: Int,
    val postId: Int,
    val authorName: String = "first_name",
    val authorLastName: String = "last_name",
    val avatarResId: Int = R.drawable.post_community_image,
    val publicationTime: String = "8th March, 14:00",
    val text: String,
    val likes: Int = 100,
)