package com.voitov.vknewsclient.presentation.authorizationScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.ui.theme.VkBlue

@Composable
fun AuthorizationScreen(
    onSigningIn: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                Text("Sign in")
            }
        }
    }
}