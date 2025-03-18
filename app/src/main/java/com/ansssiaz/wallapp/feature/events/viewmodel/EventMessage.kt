package com.ansssiaz.wallapp.feature.events.viewmodel

import arrow.core.Either
import com.ansssiaz.wallapp.feature.events.model.EventWithError
import com.ansssiaz.wallapp.feature.events.ui.EventUiModel

sealed interface EventMessage {
    data object LoadNextPage : EventMessage
    data object Refresh : EventMessage
    data class Like(val event: EventUiModel) : EventMessage
    data class Participate(val event: EventUiModel) : EventMessage
    data class Delete(val event: EventUiModel) : EventMessage
    data object HandleError : EventMessage

    data class DeleteError(val error: EventWithError) : EventMessage
    data class LikeResult(val result: Either<EventWithError, EventUiModel>) : EventMessage
    data class ParticipateResult(val result: Either<EventWithError, EventUiModel>) : EventMessage
    data class InitialLoaded(val result: Either<Throwable, List<EventUiModel>>) : EventMessage
    data class NextPageLoaded(val result: Either<Throwable, List<EventUiModel>>) : EventMessage
}