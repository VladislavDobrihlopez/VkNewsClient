package com.voitov.vknewsclient.presentation.mainScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {
    private val _authorizationState =
        MutableLiveData<AuthorizationScreenState>(AuthorizationScreenState.InitialState)
    val authorizationState: LiveData<AuthorizationScreenState>
        get() = _authorizationState

    init {
        val token = VKAccessToken.restore(VKPreferencesKeyValueStorage(application))

        Log.d(TAG, "User's token: ${token?.accessToken}")

        _authorizationState.value = if (token != null && token.isValid) {
            AuthorizationScreenState.AuthorizationSucceeded
        } else {
            AuthorizationScreenState.AuthorizationFailed
        }
    }

    fun handleAuthenticationResult(vkAuthenticationResult: VKAuthenticationResult) {
        _authorizationState.value = when (vkAuthenticationResult) {
            is VKAuthenticationResult.Success -> {
                AuthorizationScreenState.AuthorizationSucceeded
            }
            is VKAuthenticationResult.Failed -> {
                AuthorizationScreenState.AuthorizationFailed
            }
        }
    }

    companion object {
        private const val TAG = "authorization"
    }
}