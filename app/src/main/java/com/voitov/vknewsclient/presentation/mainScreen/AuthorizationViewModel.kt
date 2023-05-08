package com.voitov.vknewsclient.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.usecases.GetAuthStatusUseCase
import com.voitov.vknewsclient.domain.usecases.RetrySigningInUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val retrySigningInUseCase: RetrySigningInUseCase
) : ViewModel() {
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