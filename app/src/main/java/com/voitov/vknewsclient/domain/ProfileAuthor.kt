package com.voitov.vknewsclient.domain

import androidx.compose.runtime.Immutable

@Immutable
sealed class ProfileAuthor {
    object Me : ProfileAuthor()

    @Immutable
    data class OtherPerson(val authorId: Long) : ProfileAuthor() {
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (javaClass != other?.javaClass) return false
//
//            return true
//        }


//        override fun hashCode(): Int {
//            return authorId.hashCode()
//        }
    }

    companion object {
        const val ME = "me"
        const val OTHER = "other_person"
    }
}
