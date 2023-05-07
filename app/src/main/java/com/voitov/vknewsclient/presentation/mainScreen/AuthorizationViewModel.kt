package com.voitov.vknewsclient.presentation.mainScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepositoryImpl
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.usecases.GetAuthStatusUseCase
import com.voitov.vknewsclient.domain.usecases.RetrySigningInUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsFeedRepositoryImpl(application)
    private val getAuthStatusUseCase = GetAuthStatusUseCase(repository)
    private val retrySigningInUseCase = RetrySigningInUseCase(repository)
    val authorizationState: Flow<AuthorizationStateResult> = getAuthStatusUseCase()

    fun handleAuthenticationResult() {
        viewModelScope.launch {
            retrySigningInUseCase()
        }
    }

    companion object {
        private const val TAG = "authorization"
    }
}