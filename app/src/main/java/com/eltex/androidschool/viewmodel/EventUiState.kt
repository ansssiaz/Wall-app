package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.Event

data class EventUiState(
    val events: List<Event> = emptyList(),
)
