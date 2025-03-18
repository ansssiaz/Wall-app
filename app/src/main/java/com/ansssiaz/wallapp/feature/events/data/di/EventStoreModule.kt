package com.ansssiaz.wallapp.feature.events.data.di

import com.ansssiaz.wallapp.feature.events.effecthandler.EventEffectHandler
import com.ansssiaz.wallapp.feature.events.viewmodel.EventUiState
import com.ansssiaz.wallapp.feature.events.reducer.EventReducer
import com.ansssiaz.wallapp.feature.events.viewmodel.EventMessage
import com.ansssiaz.wallapp.feature.events.viewmodel.EventStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class EventStoreModule {
    @Provides
    fun create(
        reducer: EventReducer,
        effectHandler: EventEffectHandler
    ): EventStore = EventStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(EventMessage.Refresh),
        initState = EventUiState(),
    )

}