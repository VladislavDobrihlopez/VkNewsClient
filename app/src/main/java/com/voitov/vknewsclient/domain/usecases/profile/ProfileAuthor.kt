package com.voitov.vknewsclient.domain.usecases.profile

sealed class ProfileAuthor {
    object MINE : ProfileAuthor()
    data class OTHERS(val authorId: Long) : ProfileAuthor()
}