package com.voitov.vknewsclient.presentation.profileScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.usecases.profile.GetProfileInfoUseCase
import com.voitov.vknewsclient.domain.ProfileAuthor
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileAuthor: ProfileAuthor,
    private val useCase: GetProfileInfoUseCase
) : ViewModel() {
    val profileFlow = useCase(profileAuthor)
        .map {
            Log.d("TEST_PROFILE_WALL", it.toString())
            when (it) {
                is ProfileResult.Success -> if (it.wallContent != null) {
                    ProfileScreenState.Success.FreeProfile(
                        profileDetails = it.profileDetails,
                        wallContent = it.wallContent
                    )
                } else {
                    ProfileScreenState.Success.PrivateProfile(
                        profileDetails = it.profileDetails,
                    )
                }

                is ProfileResult.Failure -> ProfileScreenState.Failure(
                    error = it.error
                )
            }
        }
}


