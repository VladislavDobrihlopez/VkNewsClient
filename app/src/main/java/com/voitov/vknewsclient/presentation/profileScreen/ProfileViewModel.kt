package com.voitov.vknewsclient.presentation.profileScreen

import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    val profileFlow = repository.getProfileFlow("nikita983")
        .map {
            ProfileScreenState.Success(it)
        }
}