package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.reusableUIs.LoadingGoingOn

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onCommentsClickListener: (PostItem) -> Unit
) {
    val viewModel: NewsFeedScreenViewModel =
        viewModel(factory = getApplicationComponent().getViewModelsFactory())
    val postsState =
        viewModel.screenState.collectAsState(initial = NewsFeedScreenState.InitialState)
    ScreenContent(
        newsFeedScreenState = postsState,
        paddingValues = paddingValues,
        clickListener = { post ->
            onCommentsClickListener(post)
        },
        onReconnect = {
            viewModel.loadContinuingPosts()
        }
    )
}

@Composable
private fun ScreenContent(
    newsFeedScreenState: State<NewsFeedScreenState>,
    paddingValues: PaddingValues,
    clickListener: (PostItem) -> Unit,
    onReconnect: () -> Unit
) {
    when (val currentState = newsFeedScreenState.value) {
        is NewsFeedScreenState.ShowingPostsState -> {
            NewsFeedScreen(
                paddingValues,
                currentState.posts,
                currentState.isDataBeingLoaded,
                onCommentsClickListener = { post ->
                    clickListener(post)
                },
            )
        }

        is NewsFeedScreenState.LoadingState -> {
            LoadingGoingOn(modifier = Modifier.fillMaxSize())
        }

        is NewsFeedScreenState.InitialState -> {
            //do nothing
        }

        is NewsFeedScreenState.ErrorState -> {
            NoInternetLabel() {
                onReconnect()
            }
            Log.d("ERROR_TEST", "some error occurred")
        }
    }
}


