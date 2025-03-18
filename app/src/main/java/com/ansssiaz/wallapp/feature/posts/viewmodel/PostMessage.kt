package com.ansssiaz.wallapp.feature.posts.viewmodel

import arrow.core.Either
import com.ansssiaz.wallapp.feature.posts.model.PostWithError
import com.ansssiaz.wallapp.feature.posts.ui.PostUiModel

sealed interface PostMessage {
    data object LoadNextPage : PostMessage
    data object Refresh : PostMessage
    data class Like(val post: PostUiModel) : PostMessage
    data class Delete(val post: PostUiModel) : PostMessage
    data object HandleError : PostMessage

    data class DeleteError(val error: PostWithError) : PostMessage
    data class LikeResult(val result: Either<PostWithError, PostUiModel>) : PostMessage
    data class InitialLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostMessage
    data class NextPageLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostMessage
}