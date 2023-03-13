package com.voitov.vknewsclient.ui.theme.authorizationScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult

class MainViewModel : ViewModel() {
    private val _authorizationState =
        MutableLiveData<AuthorizationScreenState>(AuthorizationScreenState.InitialState)
    val authorizationState: LiveData<AuthorizationScreenState>
        get() = _authorizationState

    init {
        _authorizationState.value = if (VK.isLoggedIn()) {
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
}