package com.voitov.vknewsclient.presentation.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.ui.theme.VkBlue

@Composable
fun AuthorizationScreen(
    onSigningIn: () -> Unit
) {
    val composition =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.auth_screen_backgorund_animation))
    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            composition = composition.value,
            progress = { progress.value },
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(128.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_vk_compact_logo),
                contentDescription = "vk logo"
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = VkBlue,
                    contentColor = Color.White
                ),
                onClick = {
                    onSigningIn()
                }
            ) {
                Text(stringResource(R.string.sign_in))
            }
        }
    }
}