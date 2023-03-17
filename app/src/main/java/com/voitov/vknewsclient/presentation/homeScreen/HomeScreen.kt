package com.voitov.vknewsclient.presentation.homeScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.LoadingGoingOn
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreen
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreenState
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedViewModel

@Composable
fun HomeScreen(paddingVales: PaddingValues, onCommentsClickListener: (PostItem) -> Unit) {
    val viewModel: NewsFeedViewModel = viewModel()
    val postsState = viewModel.screenState.observeAsState(NewsFeedScreenState.InitialState)
    when (val currentState = postsState.value) {
        is NewsFeedScreenState.ShowingPostsState -> {
            NewsFeedScreen(
                paddingVales,
                currentState.posts,
                viewModel,
                currentState.isDataBeingLoaded
            ) {
                onCommentsClickListener(it)
            }
        }
        is NewsFeedScreenState.LoadingState -> {
            LoadingGoingOn(modifier = Modifier.fillMaxSize())
        }
        is NewsFeedScreenState.InitialState -> {
            //do nothing
        }
    }
}
