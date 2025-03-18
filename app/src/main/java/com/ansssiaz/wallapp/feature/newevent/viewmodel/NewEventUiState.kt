package com.ansssiaz.wallapp.feature.newevent.viewmodel

import com.ansssiaz.wallapp.feature.events.data.Event
import com.ansssiaz.wallapp.utils.Status

data class NewEventUiState(
    val event: Event? = null,
    val status: Status = Status.Idle,
)

