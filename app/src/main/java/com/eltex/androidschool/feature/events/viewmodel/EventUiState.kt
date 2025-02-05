package com.eltex.androidschool.feature.events.viewmodel

import com.eltex.androidschool.feature.events.ui.EventUiModel

data class EventUiState(
    val events: List<EventUiModel> = emptyList(),
    val status: EventStatus = EventStatus.Idle,
    val singleError: Throwable? = null,
) {
    val isEmptyError: Boolean = status is EventStatus.EmptyError
    val isRefreshing: Boolean = status == EventStatus.Refreshing
    val isEmptyLoading: Boolean = status == EventStatus.EmptyLoading
    val emptyError: Throwable? = (status as? EventStatus.EmptyError)?.reason
}
