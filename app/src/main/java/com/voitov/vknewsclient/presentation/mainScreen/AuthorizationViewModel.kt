package com.voitov.vknewsclient.presentation.mainScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepository
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsFeedRepository(application)

    val authorizationState: Flow<AuthorizationStateResult> = repository.authStatus

    fun handleAuthenticationResult() {
        viewModelScope.launch {
            repository.retrySigningIn()
        }
    }

    companion object {
        private const val TAG = "authorization"
    }
}