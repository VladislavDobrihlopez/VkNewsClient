package com.voitov.vknewsclient.presentation.newsFeedScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun NoInternetLabel(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.troubles_with_the_internet))
    }
}

@Preview
@Composable
private fun PreviewNoInternetLabel() {
    VkNewsClientTheme {
        NoInternetLabel()
    }
}