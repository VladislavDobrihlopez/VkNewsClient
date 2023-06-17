package com.voitov.vknewsclient.domain

sealed class ProfileAuthor {
    object Me : ProfileAuthor()
    data class OtherPerson(val authorId: Long) : ProfileAuthor()

    companion object {
        const val ME = "me"
        const val OTHER = "other_person"
    }
}
