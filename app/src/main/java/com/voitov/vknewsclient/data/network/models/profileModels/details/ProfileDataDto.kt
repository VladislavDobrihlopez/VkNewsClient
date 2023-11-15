package com.voitov.vknewsclient.data.network.models.profileModels.details

import com.google.gson.annotations.SerializedName

data class ProfileDataDto(
    @SerializedName("id") val id: Int,
    @SerializedName("domain") val shortenedLink: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("deactivated") val deactivated: String?,
    @SerializedName("is_closed") val isClosed: Boolean,
    @SerializedName("can_access_closed") val canAccessClosed: Boolean,
    @SerializedName("bdate") val birthday: String?,
    @SerializedName("photo_max") val photoMaxUrl: String,
    @SerializedName("about") val about: String?,
    @SerializedName("counters") val counters: CountersDto,
    @SerializedName("online") val isOnline: Int,
    @SerializedName("city") val city: CityDto?,
    @SerializedName("country") val country: CountryDto?,
    @SerializedName("cover") val cover: CoverDto?
)