package com.eltex.androidschool.feature.posts.ui

import com.eltex.androidschool.feature.posts.viewmodel.PostStatus
import com.eltex.androidschool.feature.posts.viewmodel.PostUiState
import com.eltex.androidschool.ui.PagingModel

object PostPagingMapper {
    fun map(state: PostUiState): List<PagingModel<PostUiModel>> {
        val posts = state.posts.map {
            PagingModel.Data(it)
        }
        return when (val statusValue = state.status) {
            is PostStatus.NextPageError -> posts + PagingModel.Error(statusValue.reason)
            PostStatus.NextPageLoading -> posts + PagingModel.Loading
            is PostStatus.Idle,
            PostStatus.Refreshing,
            PostStatus.EmptyLoading,
            is PostStatus.EmptyError -> posts
        }
    }
}