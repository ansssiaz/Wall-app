package com.eltex.androidschool.feature.events.model

import com.eltex.androidschool.feature.events.ui.EventUiModel

data class EventWithError(
    val event: EventUiModel,
    val throwable: Throwable,
    )