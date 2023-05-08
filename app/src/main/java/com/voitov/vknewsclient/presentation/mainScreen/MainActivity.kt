package com.voitov.vknewsclient.presentation.mainScreen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appComponent = getApplicationComponent()
            val viewModel: AuthorizationViewModel =
                viewModel(factory = appComponent.getViewModelsFactory())

            val loginLauncher = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract(),
                onResult = {
                    viewModel.handleAuthenticationResult()
                }
            )
            VkNewsClientTheme {
                val authorizationState = viewModel.authorizationState.collectAsState(
                    AuthorizationStateResult.InitialState
                )
                Log.d("AUTH_TEST", authorizationState.value.toString())

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
    }
}



