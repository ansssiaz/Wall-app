package com.ansssiaz.wallapp.feature.events.effecthandler

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ansssiaz.wallapp.feature.events.model.EventWithError
import com.ansssiaz.wallapp.feature.events.repository.EventRepository
import com.ansssiaz.wallapp.feature.events.ui.EventUiModelMapper
import com.ansssiaz.wallapp.feature.events.viewmodel.EventEffect
import com.ansssiaz.wallapp.feature.events.viewmodel.EventMessage

import com.ansssiaz.wallapp.mvi.EffectHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class EventEffectHandler @Inject constructor (
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper,
) : EffectHandler<EventEffect, EventMessage> {

    override fun connect(effects: Flow<EventEffect>): Flow<EventMessage> =
        listOf(
            handleNextPage(effects),
            handleInitialPage(effects),
            handleLike(effects),
            handleParticipate(effects),
            handleDelete(effects)
        )
            .merge()

    private fun handleNextPage(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.LoadNextPage>()
            .mapLatest {
                EventMessage.NextPageLoaded(
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

    private fun handleInitialPage(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.LoadInitialPage>()
            .mapLatest {
                EventMessage.InitialLoaded(
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

    private fun handleLike(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.Like>()
            .mapLatest {
                EventMessage.LikeResult(
                    try {
                        Either.Right(
                            mapper.map(
                                if (it.event.likedByMe) {
                                    repository.deleteLike(it.event.id)
                                } else {
                                    repository.like(it.event.id)
                                }
                            )
                        )
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Either.Left(EventWithError(it.event, e))
                    }
                )
            }

    private fun handleParticipate(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.Participate>()
            .mapLatest {
                EventMessage.ParticipateResult(
                    try {
                        Either.Right(
                            mapper.map(
                                if (it.event.participatedByMe) {
                                    repository.deleteParticipation(it.event.id)
                                } else {
                                    repository.participate(it.event.id)
                                }
                            )
                        )
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Either.Left(EventWithError(it.event, e))
                    }
                )
            }

    private fun handleDelete(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.Delete>()
            .mapLatest {
                try {
                    repository.delete(it.event.id)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    EventMessage.DeleteError(EventWithError(it.event, e))
                }
            }
            .filterIsInstance<EventMessage.DeleteError>()
}