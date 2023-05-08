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
import com.voitov.vknewsclient.NewsFeedApplication
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.presentation.ViewModelsFactory
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory

    private val component by lazy {
        (application as NewsFeedApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                val viewModel: AuthorizationViewModel = viewModel(factory = viewModelsFactory)
                val authorizationState = viewModel.authorizationState.collectAsState(
                    AuthorizationStateResult.InitialState
                )
                val loginLauncher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract(),
                    onResult = {
                        viewModel.handleAuthenticationResult()
                    }
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
                        MainScreen(viewModelsFactory = viewModelsFactory)
                    }

                    else -> {

                    }
                }
            }
        }
    }
}



