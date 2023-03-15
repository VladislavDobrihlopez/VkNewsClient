package com.voitov.vknewsclient.presentation.mainScreen

sealed class AuthorizationScreenState {
    object InitialState: AuthorizationScreenState()
    object AuthorizationSucceeded: AuthorizationScreenState()
    object AuthorizationFailed: AuthorizationScreenState()
}