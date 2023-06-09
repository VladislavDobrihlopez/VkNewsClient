package com.voitov.vknewsclient.domain.entities

data class Profile(
    val shortenedLink: String,
    val firstName: String,
    val lastName: String,
    val deactivated: String?,
    val isClosed: Boolean,
    val canAccessClosed: Boolean,
    val birthday: String,
    val photoMaxUrl: String,
    val about: String,
    val followersCount: Int,
    val isOnline: Int,
    val cityName: String?,
    val countryName: String?
)