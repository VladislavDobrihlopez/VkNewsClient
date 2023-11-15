package com.voitov.vknewsclient.data.network.models.profileModels.wall

import com.google.gson.annotations.SerializedName

data class InnerWallPostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("owner_id") val ownerId: Long,
    @SerializedName("date") val secondsSince1970: Long,
    @SerializedName("text") val text: String,
    @SerializedName("attachments") val attachments: List<AttachedPhotoDto>?,
    @SerializedName("copy_history") val copyHistory: List<InnerWallPostDto>? = null
) {
    override fun toString(): String {
        return "InnerWallPostDto(id=$id, type='$type', fromId=$fromId, ownerId=$ownerId, secondsSince1970=$secondsSince1970, text='$text', attachments=$attachments, copyHistory=$copyHistory)"
    }
}