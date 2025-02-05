package com.eltex.androidschool.feature.posts.viewmodel

import com.eltex.androidschool.feature.posts.ui.PostUiModel

sealed interface PostEffect {
    data class LoadNextPage(val id: Long, val count: Int) : PostEffect
    data class LoadInitialPage(val count: Int) : PostEffect
    data class Like(val post: PostUiModel) : PostEffect
    data class Delete(val post: PostUiModel) : PostEffect
}