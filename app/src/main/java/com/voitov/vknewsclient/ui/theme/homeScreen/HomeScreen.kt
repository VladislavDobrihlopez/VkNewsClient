package com.voitov.vknewsclient.ui.theme.homeScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.ui.theme.newsFeedScreen.NewsFeedScreen
import com.voitov.vknewsclient.ui.theme.newsFeedScreen.NewsFeedScreenState
import com.voitov.vknewsclient.ui.theme.newsFeedScreen.NewsFeedViewModel

@Composable
fun HomeScreen(paddingVales: PaddingValues, onCommentsClickListener: (Int) -> Unit) {
    val viewModel: NewsFeedViewModel = viewModel()
    val postsState = viewModel.screenState.observeAsState(NewsFeedScreenState.InitialState)
    when (val currentState = postsState.value) {
        is NewsFeedScreenState.ShowingPostsState -> {
            NewsFeedScreen(paddingVales, currentState.posts, viewModel) {
                onCommentsClickListener(it)
            }
        }
//        is NewsFeedScreenState.CommentsState -> {
//            CommentsScreen(
//                postId = currentState.postId,
//                comments = currentState.comments
//            ) {
//                viewModel.restorePreviousState()
//            }
//
//            BackHandler {
//                viewModel.restorePreviousState()
//            }
//        }
        is NewsFeedScreenState.InitialState -> {
            //do nothing
        }
    }
}

