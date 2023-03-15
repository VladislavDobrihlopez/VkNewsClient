package com.voitov.vknewsclient.presentation.mainScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import com.voitov.vknewsclient.presentation.mainScreen.MainScreen
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import com.voitov.vknewsclient.presentation.authorizationScreen.AuthorizationScreen
import com.voitov.vknewsclient.presentation.authorizationScreen.AuthorizationScreenState
import com.voitov.vknewsclient.presentation.authorizationScreen.AuthorizationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                val viewModel: AuthorizationViewModel = viewModel()
                val authorizationState = viewModel.authorizationState.observeAsState(
                    AuthorizationScreenState.InitialState
                )
                val loginLauncher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract(),
                    onResult = {
                        viewModel.handleAuthenticationResult(it)
                    }
                )
                when (authorizationState.value) {
                    AuthorizationScreenState.AuthorizationFailed -> {
                        AuthorizationScreen {
                            loginLauncher.launch(listOf(VKScope.WALL))
                        }
                    }
                    AuthorizationScreenState.AuthorizationSucceeded -> {
                        MainScreen()
                    }
                    else -> {

                    }
                }
            }
        }
    }
}



