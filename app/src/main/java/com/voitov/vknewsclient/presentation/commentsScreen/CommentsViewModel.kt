package com.voitov.vknewsclient.presentation.commentsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepository
import com.voitov.vknewsclient.domain.entities.PostItem
import kotlinx.coroutines.launch

class CommentsViewModel(
    application: Application,
    private val post: PostItem
) : AndroidViewModel(application) {
//    private val fakePostComments = mutableListOf<PostCommentItem>().apply {
//        repeat(25) {
//            add(
//                PostCommentItem(id = it, authorId = it, postId = it, text = "Some text")
//            )
//        }
//    }

    private val _screenState =
        MutableLiveData<CommentsScreenState>(CommentsScreenState.InitialState)
    val screenState: LiveData<CommentsScreenState>
        get() = _screenState

    private val repository = NewsFeedRepository(application)

    init {
        _screenState.value = CommentsScreenState.LoadingState
        viewModelScope.launch {
            val comments = repository.loadComments(post)
            //showComments(post)
            _screenState.value =
                CommentsScreenState.ShowingCommentsState(post = post, comments = comments)
        }
    }

//    private fun showComments(posts: PostItem) {
//        _screenState.value = CommentsScreenState.ShowingCommentsState(
//            post = post,
//            comments = fakePostComments
//        )
//    }
}