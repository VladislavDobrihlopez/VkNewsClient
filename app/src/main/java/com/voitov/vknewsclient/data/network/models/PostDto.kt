package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id") val id: String?,
    @SerializedName("source_id") val communityId: Long,
    @SerializedName("date") val secondsSince1970: Long,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("text") val text: String,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("comments") val comments: CommentsDto,
    @SerializedName("reposts") val reposts: RepostsDto,
    @SerializedName("views") val views: ViewsDto,
    @SerializedName("attachments") val attachments: List<AttachedPhotoDto>?


) {
    override fun toString(): String {
        return "PostDto(id='$id', communityId=$communityId, date=$secondsSince1970, isFavorite=$isFavorite, likes=$likes, comments=$comments, reposts=$reposts, views=$views, attachments=$attachments)"
    }
}