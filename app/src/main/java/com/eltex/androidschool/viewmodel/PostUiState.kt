package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status

data class PostUiState(
    val posts: List<Post>? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean = status == Status.Loading && posts?.isNotEmpty() == true
    val isEmptyLoading: Boolean = status == Status.Loading && posts.isNullOrEmpty()
    val isEmptyError: Boolean
        get() = status is Status.Error && posts.isNullOrEmpty()
    val isRefreshError: Boolean
        get() = status is Status.Error && posts?.isNotEmpty() == true
}
