package com.voitov.vknewsclient.ui.theme.homeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.voitov.vknewsclient.MainViewModel

@Composable
fun HomeScreen(paddingVales: PaddingValues, viewModel: MainViewModel) {
    val postsState = viewModel.screenState.observeAsState(HomeScreenState.InitialState)
    when (val currentState = postsState.value) {
        is HomeScreenState.NewsFeedState -> {
            NewsFeedScreen(paddingVales, currentState.posts, viewModel)
        }
        is HomeScreenState.CommentsState -> {
            CommentsScreen(
                postId = currentState.postId,
                comments = currentState.comments
            ) {
                viewModel.restorePreviousState()
            }

            BackHandler {
                viewModel.restorePreviousState()
            }
        }
        is HomeScreenState.InitialState -> {
            //do nothing
        }
    }
}

