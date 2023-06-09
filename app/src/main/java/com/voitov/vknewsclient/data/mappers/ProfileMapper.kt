package com.voitov.vknewsclient.data.mappers

import com.voitov.vknewsclient.data.network.models.profileModels.ProfileDataDto
import com.voitov.vknewsclient.domain.entities.Profile
import javax.inject.Inject

class ProfileMapper @Inject constructor() {
    fun mapDtoToEntity(dto: ProfileDataDto): Profile {
        return Profile(
            shortenedLink = dto.shortenedLink,
            firstName = dto.firstName,
            lastName = dto.lastName,
            deactivated = dto?.deactivated,
            isClosed = dto.isClosed,
            canAccessClosed = dto.canAccessClosed,
            birthday = dto.birthday,
            photoMaxUrl = dto.photoMaxUrl,
            about = dto.about,
            followersCount = dto.followersCount,
            isOnline = dto.isOnline,
            cityName = dto.city?.name,
            countryName = dto.country?.title
        )
    }
}