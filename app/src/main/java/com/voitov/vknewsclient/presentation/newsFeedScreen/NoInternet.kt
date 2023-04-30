package com.voitov.vknewsclient.presentation.newsFeedScreen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun NoInternetLabel(modifier: Modifier = Modifier) {
    Text("Troubles with the internet")
}

@Preview
@Composable
private fun PreviewNoInternetLabel() {
    VkNewsClientTheme {
        NoInternetLabel()
    }
}