package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status

data class EventUiState(
    val events: List<EventUiModel>? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean = status == Status.Loading && events?.isNotEmpty() == true
    val isEmptyLoading: Boolean = status == Status.Loading && events.isNullOrEmpty()
    val isEmptyError: Boolean
        get() = status is Status.Error && events.isNullOrEmpty()
    val isRefreshError: Boolean
        get() = status is Status.Error && events?.isNotEmpty() == true
}
