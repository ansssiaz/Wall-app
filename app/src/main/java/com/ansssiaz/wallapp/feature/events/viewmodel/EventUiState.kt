package com.ansssiaz.wallapp.feature.events.viewmodel

import com.ansssiaz.wallapp.feature.events.ui.EventUiModel

data class EventUiState(
    val events: List<EventUiModel> = emptyList(),
    val status: EventStatus = EventStatus.Idle(),
    val singleError: Throwable? = null,
) {
    val isEmptyError: Boolean = status is EventStatus.EmptyError
    val isRefreshing: Boolean = status == EventStatus.Refreshing
    val isEmptyLoading: Boolean = status == EventStatus.EmptyLoading
    val emptyError: Throwable? = (status as? EventStatus.EmptyError)?.reason
}
