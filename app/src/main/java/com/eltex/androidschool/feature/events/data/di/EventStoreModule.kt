package com.eltex.androidschool.feature.events.data.di

import com.eltex.androidschool.feature.events.effecthandler.EventEffectHandler
import com.eltex.androidschool.feature.events.viewmodel.EventUiState
import com.eltex.androidschool.feature.events.reducer.EventReducer
import com.eltex.androidschool.feature.events.viewmodel.EventMessage
import com.eltex.androidschool.feature.events.viewmodel.EventStore
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