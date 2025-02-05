package com.eltex.androidschool.feature.newevent.viewmodel

import com.eltex.androidschool.feature.events.data.Event
import com.eltex.androidschool.utils.Status

data class NewEventUiState(
    val event: Event? = null,
    val status: Status = Status.Idle,
)

