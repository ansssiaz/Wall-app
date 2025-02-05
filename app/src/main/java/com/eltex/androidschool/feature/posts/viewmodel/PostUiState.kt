package com.eltex.androidschool.feature.posts.viewmodel

import com.eltex.androidschool.feature.posts.ui.PostUiModel

data class PostUiState(
    val posts: List<PostUiModel> = emptyList(),
    val status: PostStatus = PostStatus.Idle(),
    val singleError: Throwable? = null,
) {
    val isEmptyError: Boolean = status is PostStatus.EmptyError
    val isRefreshing: Boolean = status == PostStatus.Refreshing
    val isEmptyLoading: Boolean = status == PostStatus.EmptyLoading
    val emptyError: Throwable? = (status as? PostStatus.EmptyError)?.reason
}
