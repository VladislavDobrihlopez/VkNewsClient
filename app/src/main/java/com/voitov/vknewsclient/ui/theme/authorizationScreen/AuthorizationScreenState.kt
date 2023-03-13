package com.voitov.vknewsclient.ui.theme.authorizationScreen

sealed class AuthorizationScreenState {
    object InitialState: AuthorizationScreenState()
    object AuthorizationSucceeded: AuthorizationScreenState()
    object AuthorizationFailed: AuthorizationScreenState()
}