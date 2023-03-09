package com.voitov.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.voitov.vknewsclient.ui.theme.MainScreen
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import com.voitov.vknewsclient.ui.theme.newsFeedScreen.NewsFeedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                MainScreen()
            }
        }
    }
}
