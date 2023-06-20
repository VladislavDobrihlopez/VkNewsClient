package com.voitov.vknewsclient.presentation.commentsScreen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.reusableUIs.LoadingGoingOn
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

private const val IN_PROGRESS = "..."

@Composable
fun CommentsScreen(
    post: PostItem,
    onBackPressed: () -> Unit,
    onAuthorPhotoClickListener: (PostCommentItem) -> Unit
) {
    val component = getApplicationComponent()
        .getCommentsScreenComponentFactory()
        .create(post)

    Log.d("NEWS_FEED_APPLICATION", "recomposition")

    val viewModel: CommentsViewModel = viewModel(
        factory = component.getViewModelsFactory()
    )
    val commentsState =
        viewModel.screenState.collectAsState(initial = CommentsScreenState.InitialState)

    CommentsScreenContent(
        state = commentsState,
        onAuthorPhotoClickListener = {
            onAuthorPhotoClickListener(it)
        }
    ) {
        onBackPressed()
    }
}

@Composable
private fun CommentsScreenContent(
    state: State<CommentsScreenState>,
    onAuthorPhotoClickListener: (PostCommentItem) -> Unit,
    onBackPressed: () -> Unit
) {
    when (val currentState = state.value) {
        is CommentsScreenState.LoadingState -> {
            CommentsScreenOnDataBeingLoaded(onBackPressed)
        }

        CommentsScreenState.InitialState -> {
            CommentsScreenOnDataBeingLoaded(onBackPressed)
        }

        is CommentsScreenState.DisplayCommentsState -> {
            CommentsScreenOnViewState(
                currentState.post,
                currentState.comments,
                isDataBeingLoaded = currentState.isDataBeingLoaded,
                onAuthorPhotoClickListener = {
                    onAuthorPhotoClickListener(it)
                },
                onBackPressed = {
                    onBackPressed()
                }
            )

            when (currentState) {
                is CommentsScreenState.DisplayCommentsState.CachedVersionState.FailureState -> {
                    Toast.makeText(LocalContext.current, currentState.ex.message, Toast.LENGTH_LONG)
                        .show()
                }

                is CommentsScreenState.DisplayCommentsState.CachedVersionState.EndOfCommentsState -> {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.you_have_viewed_all_the_comments),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is CommentsScreenState.DisplayCommentsState.Success -> {
                    Log.d("TEST_COMMENTS_SCREEN", "view: display")
                }
            }
        }
    }
}

@Composable
fun CommentsScreenOnDataBeingLoaded(
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = { CommentsScreenTopAppBar(onBackPressed = onBackPressed) }
    ) {
        LoadingGoingOn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
fun CommentsScreenOnViewState(
    post: PostItem,
    comments: List<PostCommentItem>,
    isDataBeingLoaded: Boolean,
    onAuthorPhotoClickListener: (PostCommentItem) -> Unit,
    onBackPressed: () -> Unit
) {
    val viewModel: CommentsViewModel = viewModel(modelClass = CommentsViewModel::class.java)

    Scaffold(
        topBar = {
            CommentsScreenTopAppBar(
                feedPostName = post.authorName,
                onBackPressed = onBackPressed
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = comments, key = { it.id }) { comment ->
                Comment(item = comment) {
                    onAuthorPhotoClickListener(comment)
                }
            }
            item {
                if (isDataBeingLoaded) {
                    LoadingGoingOn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(72.dp)
                    )
                } else {
                    SideEffect {
                        viewModel.loadCommentsContinuation()
                    }
                }
            }
        }
    }
}

@Composable
fun CommentsScreenTopAppBar(
    modifier: Modifier = Modifier,
    feedPostName: String = IN_PROGRESS,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.comments_post) + feedPostName) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.content_description_navigate_back)
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview
@Composable
private fun PreviewCommentsScreenOnDataBeingLoadedState() {
    VkNewsClientTheme {
        CommentsScreenOnDataBeingLoaded {

        }
    }
}

