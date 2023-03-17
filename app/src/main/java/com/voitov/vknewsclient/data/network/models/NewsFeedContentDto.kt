package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class NewsFeedContentDto(
    @SerializedName("items") val posts: List<PostDto>,
    @SerializedName("groups") val groups: List<GroupPhotoDto>,
    @SerializedName("next_from") val nextFrom: String?
)