package com.ansssiaz.wallapp.feature.events.viewmodel

sealed interface EventStatus {
    class Idle(val loadingFinished: Boolean = false) : EventStatus
    data object NextPageLoading : EventStatus
    data object EmptyLoading : EventStatus
    data object Refreshing : EventStatus
    data class EmptyError(val reason: Throwable) : EventStatus
    data class NextPageError(val reason: Throwable) : EventStatus
}