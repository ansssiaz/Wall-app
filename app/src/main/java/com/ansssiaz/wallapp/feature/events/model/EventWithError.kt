package com.ansssiaz.wallapp.feature.events.model

import com.ansssiaz.wallapp.feature.events.ui.EventUiModel

data class EventWithError(
    val event: EventUiModel,
    val throwable: Throwable,
    )