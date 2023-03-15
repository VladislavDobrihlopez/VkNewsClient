package com.voitov.vknewsclient.presentation.commentsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.entities.PostCommentItem

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
        showComments(postId)
    }

    private fun showComments(postId: Int) {
        _screenState.value = CommentsScreenState.ShowingCommentsState(
            postId = postId,
            comments = fakePostComments
        )
    }
}