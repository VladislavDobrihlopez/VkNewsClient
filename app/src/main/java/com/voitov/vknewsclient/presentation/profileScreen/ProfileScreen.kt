package com.voitov.vknewsclient.presentation.profileScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.ui.theme.CoolBlack
import com.voitov.vknewsclient.ui.theme.CoolWhite
import com.voitov.vknewsclient.ui.theme.Shapes
import kotlinx.coroutines.launch

private enum class SwipingStates {
    //our own enum class for stoppages e.g. expanded and collapsed
    EXPANDED,
    COLLAPSED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel =
        viewModel(factory = getApplicationComponent().getViewModelsFactory())

    val state = viewModel.profileFlow.collectAsState(
        initial = Profile(
            "",
            "",
            "",
            "",
            true,
            true,
            "",
            "",
            "",
            0,
            1,
            cityName = null,
            null
        )
    )

    when (val screenState = state.value) {
        is ProfileScreenState.Success -> Profile(profileInfo = screenState.profileInfo)
        is ProfileScreenState.Failure -> Failure(errorMessage = screenState.errorMessage)
    }
}

@Composable
private fun Failure(errorMessage: String) {
    Text(
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center,
        text = errorMessage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Profile(profileInfo: Profile) {
    val scope = rememberCoroutineScope()
    val modalWindowState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    BackHandler(modalWindowState.isVisible) {
        scope.launch { modalWindowState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = modalWindowState,
        sheetContent = {
            ProfileDetails(profileInfo)
        }) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val progress = rememberSaveable {
                mutableStateOf(0f)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Slider(
                value = progress.value,
                onValueChange = {
                    progress.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 32.dp)
            )

            val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)
            val heightInPx = with(LocalDensity.current) {
                maxHeight.toPx()
            }
            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        val delta = available.y
                        return if (delta < 0) {
                            swipingState.performDrag(delta).toOffset()
                        } else {
                            Offset.Zero
                        }
                    }

                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        val delta = available.y
                        return swipingState.performDrag(delta).toOffset()
                    }

                    override suspend fun onPostFling(
                        consumed: Velocity,
                        available: Velocity
                    ): Velocity {
                        swipingState.performFling(velocity = available.y)
                        return super.onPostFling(consumed, available)
                    }

                    private fun Float.toOffset() = Offset(0f, this)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .swipeable(
                        state = swipingState,
                        thresholds = { _, _ ->
                            FractionalThreshold(0.15f)//it can be 0.5 in general
                        },
                        orientation = Orientation.Vertical,
                        anchors = mapOf(
                            0f to SwipingStates.COLLAPSED,//min height is collapsed
                            heightInPx to SwipingStates.EXPANDED,//max height is expanded
                        )
                    )
                    .nestedScroll(nestedScrollConnection)
            ) {
                val computedProgress by remember {//progress value will be decided as par state
                    derivedStateOf {
                        if (swipingState.progress.to == SwipingStates.COLLAPSED)
                            swipingState.progress.fraction
                        else
                            1f - swipingState.progress.fraction
                    }
                }
                val startHeightNum = 238

                ProfileHeader(
                    progress = computedProgress, profileInfo, onDetailsClicked = {
                        scope.launch {
                            modalWindowState.show()
                        }
                    },
                    onProgressChange = {
                        progress.value = it
                    })
            }
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun ProfileHeader(
    progress: Float,
    profileInfo: Profile,
    onDetailsClicked: () -> Unit,
    onProgressChange: (Float) -> Unit
) {
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(shape = Shapes.medium)
            .background(Color.Green)
    ) {
        val profilePictureCustomProperties = motionProperties("profile_picture")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId("background")
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CoolBlack)
                .padding(top = 12.dp)
                .layoutId("wall_content")
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 120.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(50, key = { it }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(color = Color.Black, text = it.toString())
                    }
                }
            }
        }
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
        AsyncImage(
            placeholder = painterResource(id = R.drawable.profile_placeholder),
            model = profileInfo.photoMaxUrl,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = profilePictureCustomProperties.value.color("border_color"),
                    shape = CircleShape
                )
                .layoutId("profile_picture"),
            contentDescription = null
        )
//        Image(
//            painter = painterResource(id = R.drawable.post_content_image),
//            modifier = Modifier
//                .size(36.dp)
//                .clip(CircleShape)
//                .border(
//                    width = 2.dp,
//                    color = profilePictureCustomProperties.value.color("border_color"),
//                    shape = CircleShape
//                )
//                .layoutId("profile_picture"),
//            contentDescription = "picture"
//        )
        Text(
            text = "${profileInfo.firstName} ${profileInfo.lastName}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .layoutId("username")
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
                    text = "Followers: ${profileInfo.followersCount}",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = Shapes.medium),
                onClick = { onDetailsClicked() }) {
                Text(text = "See details")
            }
        }
    }
}

@Composable
private fun ProfileDetails(profileInfo: Profile) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "Short_link: ${profileInfo.shortenedLink}")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Cake, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "Birthday: ${profileInfo.birthday}")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Flag, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "Country: ${profileInfo.countryName}")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.LocationCity, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(modifier = Modifier.weight(1f), text = "City: ${profileInfo.cityName}")
        }
    }
}