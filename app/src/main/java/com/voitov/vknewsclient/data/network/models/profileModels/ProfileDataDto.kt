package com.voitov.vknewsclient.data.network.models.profileModels

import com.google.gson.annotations.SerializedName

data class ProfileDataDto(
    @SerializedName("domain") val shortenedLink: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("deactivated") val deactivated: String?,
    @SerializedName("is_closed") val isClosed: Boolean,
    @SerializedName("can_access_closed") val canAccessClosed: Boolean,
    @SerializedName("bdate") val birthday: String?,
    @SerializedName("photo_max") val photoMaxUrl: String,
    @SerializedName("about") val about: String?,
    @SerializedName("followers_count") val followersCount: Int,
    @SerializedName("online") val isOnline: Int,
    @SerializedName("city") val city: CityDto?,
    @SerializedName("country") val country: CountryDto?,
    @SerializedName("cover") val cover: CoverDto
)