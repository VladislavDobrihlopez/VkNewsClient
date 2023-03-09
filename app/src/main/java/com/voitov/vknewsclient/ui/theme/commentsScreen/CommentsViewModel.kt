package com.voitov.vknewsclient.ui.theme.commentsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.PostCommentItem

class CommentsViewModel(
    private val postId: Int
) : ViewModel() {
    private val fakePostComments = mutableListOf<PostCommentItem>().apply {
        repeat(25) {
            add(
                PostCommentItem(id = it, authorId = it, postId = it, text = "Some text")
            )
        }
    }

    private val _screenState =
        MutableLiveData<CommentsScreenState>(CommentsScreenState.InitialState)
    val screenState: LiveData<CommentsScreenState>
        get() = _screenState

    init {
        showComments(1)
    }

    private fun showComments(postId: Int) {
        _screenState.value = CommentsScreenState.ShowingCommentsState(
            postId = postId,
            comments = fakePostComments
        )
    }
}