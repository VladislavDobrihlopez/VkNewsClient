package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class CommentLikeInfoDto(
    @SerializedName("count") val count: Int,
    @SerializedName("user_likes") val isLikedByUser: Int
)