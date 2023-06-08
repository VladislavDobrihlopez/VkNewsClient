package com.voitov.vknewsclient.presentation.profileScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.ui.theme.CoolBlack
import com.voitov.vknewsclient.ui.theme.CoolWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen() {
    val scope = rememberCoroutineScope()
    val modalWindowState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    BackHandler(modalWindowState.isVisible) {
        scope.launch { modalWindowState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = modalWindowState,
        sheetContent = {
            ProfileDetails()
        }) {
        Column(modifier = Modifier.fillMaxSize()) {
            val progress = rememberSaveable {
                mutableStateOf(0f)
            }

            ProfileHeader(progress.value) {
                scope.launch {
                    modalWindowState.show()
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Slider(
                value = progress.value,
                onValueChange = {
                    progress.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalMotionApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ProfileHeader(progress: Float, onDetailsClicked: () -> Unit) {
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
        modifier = Modifier.fillMaxWidth()
    ) {
        val profilePictureCustomProperties = motionProperties("profile_picture")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId("background")
        )
        Image(
            modifier = Modifier.layoutId("cover"),//.aspectRatio(3f/1f),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.test_cover),
            contentDescription = null
        )
        Divider(
            modifier = Modifier.layoutId("divider"),
            color = if (isSystemInDarkTheme()) CoolWhite else CoolBlack
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("social_metrics")
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Friends: 150",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Subscribers: 150",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onDetailsClicked() }) {
                Text(text = "See details")
            }
        }
    }
}

@Composable
private fun ProfileDetails() {
    Column(modifier = Modifier.padding(8.dp).fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "Short_link: ")
        }
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Icon(imageVector = Icons.Default.Face, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "Birthday: ")
        }
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Icon(imageVector = Icons.Default.Home, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "City: ")
        }
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "Work: ")
        }
    }
}