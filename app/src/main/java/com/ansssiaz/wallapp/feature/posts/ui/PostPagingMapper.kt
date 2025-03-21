package com.ansssiaz.wallapp.feature.posts.ui

import com.ansssiaz.wallapp.feature.posts.viewmodel.PostStatus
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostUiState
import com.ansssiaz.wallapp.ui.PagingModel

object PostPagingMapper {
    private const val LOADING_ITEM_COUNT = 10
    fun map(state: PostUiState): List<PagingModel<PostUiModel>> {
        val posts = state.posts.map {
            PagingModel.Data(it)
        }
        return when (val statusValue = state.status) {
            is PostStatus.NextPageError -> posts + PagingModel.Error(statusValue.reason)

            PostStatus.NextPageLoading -> {
                val loadingSkeletons = List(LOADING_ITEM_COUNT) { PagingModel.PostSkeleton }
                posts + loadingSkeletons
            }

            PostStatus.EmptyLoading -> List(LOADING_ITEM_COUNT) { PagingModel.PostSkeleton }

            is PostStatus.Idle,
            PostStatus.Refreshing,
            is PostStatus.EmptyError -> posts
        }
    }
}