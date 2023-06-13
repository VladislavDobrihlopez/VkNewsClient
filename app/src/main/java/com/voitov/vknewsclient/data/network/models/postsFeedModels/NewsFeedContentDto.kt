package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class NewsFeedContentDto(
    @SerializedName("items") val posts: List<PostDto>,
    @SerializedName("groups") val groups: List<GroupInfoDto>,
    @SerializedName("next_from") val nextFrom: String?
)