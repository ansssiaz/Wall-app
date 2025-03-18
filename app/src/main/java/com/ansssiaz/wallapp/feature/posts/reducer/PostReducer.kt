package com.ansssiaz.wallapp.feature.posts.reducer

import arrow.core.Either
import com.ansssiaz.wallapp.feature.posts.ui.PostUiModel
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostEffect
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostMessage
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostStatus
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostUiState
import com.ansssiaz.wallapp.mvi.Reducer
import com.ansssiaz.wallapp.mvi.ReducerResult
import javax.inject.Inject

class PostReducer @Inject constructor(): Reducer<PostUiState, PostEffect, PostMessage> {
    private companion object {
        const val PAGE_SIZE = 10

    }

    override fun reduce(
        old: PostUiState,
        message: PostMessage
    ): ReducerResult<PostUiState, PostEffect> =
        when (message) {
            is PostMessage.Delete -> {
                ReducerResult(
                    old.copy(
                        posts = old.posts.filter { it.id != message.post.id },
                    ),
                    PostEffect.Delete(message.post)
                )
            }

            is PostMessage.DeleteError -> {
                ReducerResult(
                    old.copy(
                        posts = buildList(old.posts.size + 1) {
                            val post = message.error.post
                            extracted(old, post)
                            add(post)
                            addAll(old.posts.filter { it.id < post.id })
                        }
                    ),
                )
            }

            PostMessage.HandleError -> {
                ReducerResult(old.copy(singleError = null))
            }

            is PostMessage.InitialLoaded -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        if (old.posts.isNotEmpty()) {
                            old.copy(singleError = messageResult.value, status = PostStatus.Idle())
                        } else {
                            old.copy(status = PostStatus.EmptyError(messageResult.value))
                        }
                    }

                    is Either.Right -> old.copy(
                        posts = messageResult.value,
                        status = PostStatus.Idle()
                    )
                }
            )

            is PostMessage.Like -> {
                ReducerResult(
                    old.copy(
                        posts = old.posts.map {
                            if (it.id == message.post.id) {
                                it.copy(
                                    likedByMe = !it.likedByMe,
                                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                                )
                            } else {
                                it
                            }
                        }
                    ),
                    PostEffect.Like(message.post)
                )
            }

            is PostMessage.LikeResult -> {
                ReducerResult(
                    when (val result = message.result) {
                        is Either.Left -> {
                            val value = result.value
                            val post = value.post
                            old.copy(
                                posts = old.posts.map {
                                    if (it.id == post.id) {
                                        post
                                    } else {
                                        it
                                    }
                                },
                                singleError = value.throwable,
                            )
                        }

                        is Either.Right -> {
                            val post = result.value
                            old.copy(
                                posts = old.posts.map {
                                    if (it.id == post.id) {
                                        post
                                    } else {
                                        it
                                    }
                                },
                            )
                        }
                    }
                )
            }

            PostMessage.LoadNextPage -> {
                val loadingFinished = (old.status as? PostStatus.Idle)?.loadingFinished == true
                val effect = if (loadingFinished) {
                    null
                } else {
                    PostEffect.LoadNextPage(old.posts.last().id, PAGE_SIZE)
                }

                val status = if (loadingFinished) {
                    old.status
                } else {
                    PostStatus.NextPageLoading
                }

                ReducerResult(
                    old.copy(status = status), effect,
                )
            }

            is PostMessage.NextPageLoaded -> {
                ReducerResult(
                    when (message.result) {
                        is Either.Left -> {
                            old.copy(status = PostStatus.NextPageError(message.result.value))
                        }

                        is Either.Right -> {
                            val postUiModels = message.result.value
                            val loadingFinished = postUiModels.size < PAGE_SIZE
                            old.copy(
                                posts = old.posts + message.result.value,
                                status = PostStatus.Idle(loadingFinished),
                            )
                        }
                    }
                )
            }

            PostMessage.Refresh -> {
                ReducerResult(
                    old.copy(
                        status = if (old.posts.isNotEmpty()) {
                            PostStatus.Refreshing
                        } else {
                            PostStatus.EmptyLoading
                        }
                    ),
                    PostEffect.LoadInitialPage(PAGE_SIZE),
                )
            }
        }

    private fun MutableList<PostUiModel>.extracted(
        old: PostUiState,
        post: PostUiModel
    ) {
        addAll(old.posts.filter { it.id > post.id })
    }
}