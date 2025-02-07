package com.eltex.androidschool.feature.events.data.di

import com.eltex.androidschool.feature.events.effecthandler.EventEffectHandler
import com.eltex.androidschool.feature.events.viewmodel.EventUiState
import com.eltex.androidschool.feature.events.reducer.EventReducer
import com.eltex.androidschool.feature.events.repository.EventRepository
import com.eltex.androidschool.feature.events.ui.EventUiModelMapper
import com.eltex.androidschool.feature.events.viewmodel.EventMessage
import com.eltex.androidschool.feature.events.viewmodel.EventStore


class EventStoreFactory(private val repository: EventRepository) {
    fun create(): EventStore = EventStore(
        EventReducer(),
        EventEffectHandler(
            repository,
            EventUiModelMapper()
        ),
        setOf(EventMessage.Refresh),
        EventUiState(),
    )

}