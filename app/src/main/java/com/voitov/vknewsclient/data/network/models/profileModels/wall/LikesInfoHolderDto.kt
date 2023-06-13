package com.voitov.vknewsclient.data.network.models.profileModels.wall

import com.google.gson.annotations.SerializedName

data class LikesInfoHolderDto(
    @SerializedName("count") val count: Int,
    @SerializedName("user_likes") val userLikes: Int
)