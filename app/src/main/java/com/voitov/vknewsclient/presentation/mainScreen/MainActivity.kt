package com.voitov.vknewsclient.presentation.mainScreen

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                val appComponent = getApplicationComponent()
                val viewModel: AuthorizationViewModel =
                    viewModel(factory = appComponent.getViewModelsFactory())

                val authorizationState = viewModel.authorizationState.collectAsState(
                    AuthorizationStateResult.InitialState
                )
                Log.d("AUTH_TEST", authorizationState.value.toString())

                Authorization(
                    viewModel = viewModel,
                    authorizationState = authorizationState
                )
            }
        }
    }

    @Composable
    private fun Authorization(
        viewModel: AuthorizationViewModel,
        authorizationState: State<AuthorizationStateResult>
    ) {
        val loginLauncher = rememberLauncherForActivityResult(
            contract = VK.getVKAuthActivityResultContract(),
            onResult = {
                viewModel.handleAuthenticationResult()
            }
        )

        when (authorizationState.value) {
            AuthorizationStateResult.AuthorizationStateFailure -> {
                Log.d("AUTH_TEST", "failure")

                AuthorizationScreen {
                    loginLauncher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                }
            }

            AuthorizationStateResult.AuthorizationStateSuccess -> {
                MainScreen()
            }

            else -> {

            }
        }
    }
}



