package com.eltex.androidschool.feature.posts.viewmodel

sealed interface PostStatus {
    data class Idle(val loadingFinished: Boolean = false) : PostStatus
    data object NextPageLoading : PostStatus
    data object EmptyLoading : PostStatus
    data object Refreshing : PostStatus
    data class EmptyError(val reason: Throwable) : PostStatus
    data class NextPageError(val reason: Throwable) : PostStatus
}