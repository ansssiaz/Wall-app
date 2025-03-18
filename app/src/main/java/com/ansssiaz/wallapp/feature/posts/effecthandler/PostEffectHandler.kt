package com.ansssiaz.wallapp.feature.posts.effecthandler

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ansssiaz.wallapp.feature.posts.model.PostWithError
import com.ansssiaz.wallapp.feature.posts.repository.PostRepository
import com.ansssiaz.wallapp.feature.posts.ui.PostUiModelMapper
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostEffect
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostMessage
import com.ansssiaz.wallapp.mvi.EffectHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class PostEffectHandler @Inject constructor(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper,
) : EffectHandler<PostEffect, PostMessage> {

    override fun connect(effects: Flow<PostEffect>): Flow<PostMessage> =
        listOf(
            handleNextPage(effects),
            handleInitialPage(effects),
            handleLike(effects),
            handleDelete(effects)
        )
            .merge()

    private fun handleNextPage(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.LoadNextPage>()
            .mapLatest {
                PostMessage.NextPageLoaded(
                    try {
                        repository.getBefore(it.id, it.count)
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    private fun handleInitialPage(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.LoadInitialPage>()
            .mapLatest {
                PostMessage.InitialLoaded(
                    try {
                        repository.getLatest(it.count)
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    private fun handleLike(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.Like>()
            .mapLatest {
                PostMessage.LikeResult(
                    try {
                        Either.Right(
                            mapper.map(
                                if (it.post.likedByMe) {
                                    repository.deleteLike(it.post.id)
                                } else {
                                    repository.like(it.post.id)
                                }
                            )
                        )
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Either.Left(PostWithError(it.post, e))
                    }
                )
            }

    private fun handleDelete(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.Delete>()
            .mapLatest {
                try {
                    repository.delete(it.post.id)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    PostMessage.DeleteError(PostWithError(it.post, e))
                }
            }
            .filterIsInstance<PostMessage.DeleteError>()
}