package com.voitov.vknewsclient.presentation.newsFeedScreen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.ui.theme.VkBlue
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun NoInternetLabel(
    modifier: Modifier = Modifier,
    onReconnect: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val rotationDegreeState = infiniteTransition.animateFloat(
                initialValue = -95f,
                targetValue = 95f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 10000),
                    repeatMode = RepeatMode.Reverse
                )
            )

            ErrorImage(rotationDegreeState)

            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(R.string.troubles_with_the_internet))

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = VkBlue,
                    contentColor = Color.White
                ),
                onClick = {
                    onReconnect()
                }
            ) {
                Text(stringResource(R.string.try_reconnecting))
            }
        }
    }
}

@Composable
private fun ErrorImage(
    rotationDegree: State<Float>
) {
    Image(
        modifier = Modifier
            .size(128.dp)
            .rotate(degrees = rotationDegree.value),
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_connection),
        contentDescription = "no connection to the internet"
    )
}

@Preview
@Composable
private fun PreviewNoInternetLabel() {
    VkNewsClientTheme {
        NoInternetLabel() {

        }
    }
}