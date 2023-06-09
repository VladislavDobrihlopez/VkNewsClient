package com.voitov.vknewsclient.data.network.models.profileModels

import com.google.gson.annotations.SerializedName

data class ProfileWallContentDto(
    @SerializedName("items") val posts: List<WallPostDto>,
)