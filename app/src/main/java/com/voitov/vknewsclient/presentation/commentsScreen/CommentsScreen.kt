package com.voitov.vknewsclient.presentation.commentsScreen

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.LoadingGoingOn

@Composable
fun CommentsScreen(
    post: PostItem,
    onBackPressed: () -> Unit
) {
    val viewModel: CommentsViewModel = viewModel(
        factory = CommentsViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            post = post
        )
    )
    val commentsState = viewModel.screenState.observeAsState(CommentsScreenState.InitialState)

    when (val currentState = commentsState.value) {
        is CommentsScreenState.LoadingState -> {
            CommentsScreenOnDataBeingLoadedState(currentState, onBackPressed)
        }
        is CommentsScreenState.ShowingCommentsState -> {
            CommentsScreenOnViewState(currentState, onBackPressed)
        }
        CommentsScreenState.InitialState -> {
        }
    }
}

@Composable
fun CommentsScreenOnDataBeingLoadedState(
    currentState: CommentsScreenState.LoadingState,
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
    currentState: CommentsScreenState.ShowingCommentsState,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            CommentsScreenTopAppBar(
                commentsState = currentState,
                onBackPressed = onBackPressed
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = currentState.comments, key = { it.id }) { comment ->
                Comment(item = comment)
            }
        }
    }
}

@Composable
fun CommentsScreenTopAppBar(
    modifier: Modifier = Modifier,
    commentsState: CommentsScreenState.ShowingCommentsState? = null,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "Comments post: ${commentsState?.post?.communityId ?: "..."}") },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        }
    )
}
