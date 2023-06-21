package com.voitov.vknewsclient.domain

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ProfileAuthor {
    @Immutable
    object Me : ProfileAuthor

    @Immutable
    data class OtherPerson(val authorId: Long) : ProfileAuthor

    companion object {
        const val ME = "me"
        const val OTHER = "other_person"
    }
}
