package com.voitov.vknewsclient.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.mainScreen.AuthorizationScreen
import com.voitov.vknewsclient.presentation.mainScreen.AuthorizationViewModel
import com.voitov.vknewsclient.presentation.mainScreen.MainScreen
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        )
        installSplashScreen()
        setContent {
            val appComponent = getApplicationComponent()
            val viewModel: AuthorizationViewModel =
                viewModel(factory = appComponent.getViewModelsFactory())

            val authorizationState = viewModel.authorizationState.collectAsState(
                AuthorizationStateResult.InitialState
            )

            VkNewsClientTheme {
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
    }
}



