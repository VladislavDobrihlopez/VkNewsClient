package com.voitov.vknewsclient.presentation.profileScreen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.reusableUIs.LoadingGoingOn
import com.voitov.vknewsclient.presentation.reusableUIs.PostAdditionalPhotos
import com.voitov.vknewsclient.presentation.reusableUIs.PostCard
import com.voitov.vknewsclient.presentation.reusableUIs.PostText
import com.voitov.vknewsclient.ui.theme.CoolBlack
import com.voitov.vknewsclient.ui.theme.CoolWhite
import com.voitov.vknewsclient.ui.theme.Shapes
import kotlinx.coroutines.launch

private enum class SwipingStates {
    //our own enum class for stoppages e.g. expanded and collapsed
    EXPANDED,
    COLLAPSED
}

@Composable
fun ProfileScreen(author: ProfileAuthor) {
    val component =
        getApplicationComponent()
            .getProfileScreenComponentFactory()
            .create(author)

    val viewModel: ProfileViewModel =
        viewModel(factory = component.getViewModelsFactory())

    Log.d("TEST_R", "recomposition: $viewModel}")

    val state =
        viewModel.profileFlow.collectAsState(
            initial = ProfileScreenState.InitialState
        )

    ProfileScreenContent(state = state) {
        Log.d("TEST_R", "callback: $viewModel}")
        viewModel.loadContinuingWallPosts()
    }
}

@Composable
fun ProfileScreenContent(state: State<ProfileScreenState>, onEndOfWallPosts: () -> Unit) {
    when (val screenState = state.value) {
        is ProfileScreenState.SuccessState -> {
            Profile(
                profileInfo = screenState.profileDetails,
            ) {
                if (screenState is ProfileScreenState.SuccessState.ProfileWithWall) {
                    PostFeed(content = screenState.wallContent, screenState.isDataBeingLoaded) {
                        onEndOfWallPosts()
                    }
                    if (screenState is ProfileScreenState.SuccessState.ProfileWithWall.EndOfPostsState) {
                        Toast.makeText(LocalContext.current, "that's all", Toast.LENGTH_SHORT).show()
                    }
                } else if (screenState is ProfileScreenState.SuccessState.PrivateProfileState) {
                    UnavailableAsNoAccess(modifier = Modifier.fillMaxSize())
                }
            }
        }

        is ProfileScreenState.FailureState -> Failure(errorMessage = screenState.error)
        is ProfileScreenState.InitialState -> {
            LoadingGoingOn(modifier = Modifier.padding(8.dp))
        }

        ProfileScreenState.LoadingState -> {
            LoadingGoingOn(modifier = Modifier.padding(8.dp))
        }
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

@Composable
private fun UnavailableAsNoAccess(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "This profile is private. Add them as a friend to see their posts, photos and other content"
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Profile(
    profileInfo: Profile,
    WallContent: @Composable BoxScope.() -> Unit
) {
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
                        thresholds = { stateFrom, stateTo ->
                            if (stateFrom == SwipingStates.COLLAPSED) {
                                FractionalThreshold(0.75f)//it can be 0.5 in general
                            } else {
                                FractionalThreshold(0.15f)
                            }
                        },
                        orientation = Orientation.Vertical,
                        anchors = mapOf(
                            0f to SwipingStates.COLLAPSED,//min height is collapsed
                            heightInPx to SwipingStates.EXPANDED,//max height is expanded
                        )
                    )
                    .nestedScroll(nestedScrollConnection)
            ) {
                Log.d("TEST_MOTION", "box recomposition")
                val computedProgress = remember {//progress value will be decided as par state
                    derivedStateOf {
                        if (swipingState.progress.to == SwipingStates.COLLAPSED) {
                            swipingState.progress.fraction
                        } else {
                            1f - swipingState.progress.fraction
                        }
                    }
                }
                UserProfile(
                    progress = computedProgress, profileInfo, onDetailsClicked = {
                        scope.launch {
                            modalWindowState.show()
                        }
                    }) {
                    WallContent(this)
                }
            }
        }
    }
}

@Composable
private fun PostFeed(
    content: List<WallPost>,
    isDataBeingLoaded: Boolean,
    onEndOfWallPosts: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 136.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = content, key = { it.id }) { wallPost ->
            val postItem = PostItem(
                id = wallPost.id,
                communityPhotoUrl = wallPost.producerPhotoUrl,
                communityId = wallPost.producerId,
                authorName = wallPost.producerName,
                date = wallPost.date,
                contentText = wallPost.contentText,
                isLikedByUser = wallPost.isLikedByUser,
                isSharedByUser = wallPost.isSharedByUser,
                contentImageUrl = wallPost.contentImageUrl,
                metrics = wallPost.metrics,
                dateInMillis = 0,
            )
            PostCard(
                postItem = postItem,
                PostContent = {
                    PostText(text = postItem.contentText)
                    wallPost.postLifecycleHierarchy.forEach { innerPost ->
                        PostCard(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            postItem = PostItem(
                                id = innerPost.id,
                                communityPhotoUrl = innerPost.producerPhotoUrl,
                                communityId = innerPost.producerId,
                                authorName = innerPost.producerName,
                                date = innerPost.date,
                                contentText = innerPost.contentText,
                                isLikedByUser = false,
                                isSharedByUser = false,
                                contentImageUrl = innerPost.contentImageUrl ?: listOf(),
                                metrics = listOf(),
                                dateInMillis = 0,
                            ),
                            PostFeedbackContent = {

                            },
                            PostHeaderContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Repeat,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                    AsyncImage(
                                        model = innerPost.producerPhotoUrl,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(25.dp),
                                        contentDescription = "community thumbnail"
                                    )
                                    Spacer(modifier = Modifier.padding(all = 4.dp))
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            innerPost.producerName,
                                            color = MaterialTheme.colors.onPrimary
                                        )
                                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                                        Text(
                                            innerPost.date,
                                            color = MaterialTheme.colors.onSecondary
                                        )
                                    }
                                }
                            }
                        )
                    }

                    PostAdditionalPhotos(contentImgUrls = wallPost.contentImageUrl)
                }
            )
        }
        item {
            if (isDataBeingLoaded) {
                LoadingGoingOn(modifier = Modifier.fillMaxWidth())
            } else {
                SideEffect {
                    onEndOfWallPosts()
                }
            }
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun UserProfile(
    progress: State<Float>,
    profileInfo: Profile,
    onDetailsClicked: () -> Unit,
    WallContent: @Composable BoxScope.() -> Unit
) {
    Log.d("TEST_MOTION", "user profile recomposition")

    val context = LocalContext.current
    val motionSceneData = remember {
        context.resources
            .openRawResource(R.raw.motion_scene_for_profile_screen)
            .readBytes()
            .decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(content = motionSceneData),
        progress = progress.value,
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
                .background(if (isSystemInDarkTheme()) CoolBlack else Color.White)
                .layoutId("background")
        )
        Box(
            modifier = Modifier
                //.fillMaxSize()
                .background(color = if (isSystemInDarkTheme()) CoolBlack else CoolWhite)
                .padding(top = 12.dp)
                .layoutId("wall_content")
        ) {
            WallContent(this)
        }
        AsyncImage(
            model = profileInfo.coverPhotoUrl,
            modifier = Modifier
                .layoutId("cover")
                .aspectRatio(3f / 1f),
            contentScale = ContentScale.FillBounds,
            //placeholder = painterResource(id = R.drawable.test_cover),
            contentDescription = null
        )
        Divider(
            modifier = Modifier.layoutId("divider"),
            color = Color.White
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

        Text(
            text = "${profileInfo.firstName} ${profileInfo.lastName}",
            color = if (isSystemInDarkTheme()) CoolWhite else CoolBlack,
            fontSize = 20.sp,
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
                    color = if (isSystemInDarkTheme()) CoolWhite else CoolBlack,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Followers: ${profileInfo.followersCount}",
                    color = if (isSystemInDarkTheme()) CoolWhite else CoolBlack,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = Shapes.medium),

                onClick = { onDetailsClicked() }) {
                Text(
                    text = "See details",
                    color = if (isSystemInDarkTheme()) CoolWhite else CoolBlack,
                )
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = if (isSystemInDarkTheme()) Color.White else CoolBlack
                )
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
            Text(modifier = Modifier.weight(1f), text = "Short_link: @${profileInfo.shortenedLink}")
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