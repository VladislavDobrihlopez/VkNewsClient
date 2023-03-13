package com.voitov.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import com.voitov.vknewsclient.ui.theme.MainScreen
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import com.voitov.vknewsclient.ui.theme.authorizationScreen.AuthorizationScreen
import com.voitov.vknewsclient.ui.theme.authorizationScreen.AuthorizationScreenState
import com.voitov.vknewsclient.ui.theme.authorizationScreen.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                val viewModel: MainViewModel = viewModel()
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



