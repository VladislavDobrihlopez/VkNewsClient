package com.voitov.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.voitov.vknewsclient.ui.theme.MainViewModel
import com.voitov.vknewsclient.ui.theme.VkNews
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                VkNews(viewModel)
            }
        }
    }
}
