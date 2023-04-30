package com.voitov.vknewsclient.domain

sealed class AuthorizationStateResult {
    object InitialState: AuthorizationStateResult()
    object AuthorizationStateSuccess: AuthorizationStateResult()
    object AuthorizationStateFailure: AuthorizationStateResult()
}