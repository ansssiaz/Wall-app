package com.eltex.androidschool.feature.events.viewmodel

sealed interface EventStatus {
    data object Idle : EventStatus
    data object NextPageLoading : EventStatus
    data object EmptyLoading : EventStatus
    data object Refreshing : EventStatus
    data class EmptyError(val reason: Throwable) : EventStatus
    data class NextPageError(val reason: Throwable) : EventStatus
}