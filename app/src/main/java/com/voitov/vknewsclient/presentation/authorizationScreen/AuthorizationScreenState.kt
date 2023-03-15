package com.voitov.vknewsclient.presentation.authorizationScreen

sealed class AuthorizationScreenState {
    object InitialState: AuthorizationScreenState()
    object AuthorizationSucceeded: AuthorizationScreenState()
    object AuthorizationFailed: AuthorizationScreenState()
}