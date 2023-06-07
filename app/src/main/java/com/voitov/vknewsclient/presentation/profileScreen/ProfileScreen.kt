package com.voitov.vknewsclient.presentation.profileScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.voitov.vknewsclient.R

@Composable
fun ProfileScreen() {
    Column {
        var progress by remember {
            mutableStateOf(0f)
        }
        ProfileHeader(progress = progress)
        Spacer(modifier = Modifier.height(32.dp))
        Slider(
            value = progress,
            onValueChange = {
                progress = it
            },
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun ProfileHeader(progress: Float) {
    Log.d("TEST_HEADER", progress.toString())
    val context = LocalContext.current
    val motionSceneData = remember {
        context.resources
            .openRawResource(R.raw.motion_scene_for_profile_screen)
            .readBytes()
            .decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(content = motionSceneData),
        progress = progress,
        modifier = Modifier.fillMaxWidth().background(Color.DarkGray)

    ) {
        val profilePictureCustomProperties = motionProperties("profile_picture")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("background")
        )
        Image(
            painter = painterResource(id = R.drawable.post_content_image),
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = profilePictureCustomProperties.value.color("border_color"),
                    shape = CircleShape
                )
                .layoutId("profile_picture"),
            contentDescription = "picture"
        )
        Text(
            text = "Maks Korzh",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.layoutId("username")
        )
    }
}