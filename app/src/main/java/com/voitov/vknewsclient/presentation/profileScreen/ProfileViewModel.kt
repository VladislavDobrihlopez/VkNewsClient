package com.voitov.vknewsclient.presentation.profileScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.usecases.profile.GetProfileInfoUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val useCase: GetProfileInfoUseCase
) : ViewModel() {
    val profileFlow = useCase("dobrihlopez").map {
        Log.d("TEST_PROFILE_WALL", it.toString())
        when (it) {
            is ProfileResult.Success -> ProfileScreenState.Success(
                profileDetails = it.profileDetails,
                wallContent = it.wallContent
            )

            is ProfileResult.Failure -> ProfileScreenState.Failure(
                error = it.error
            )
        }
    }
}