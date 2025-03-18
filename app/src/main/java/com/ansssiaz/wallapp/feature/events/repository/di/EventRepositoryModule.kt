package com.ansssiaz.wallapp.feature.events.repository.di

import com.ansssiaz.wallapp.feature.events.repository.EventRepository
import com.ansssiaz.wallapp.feature.events.repository.NetworkEventRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface EventRepositoryModule {
    @Binds
    fun bindNetworkEventRepository(impl: NetworkEventRepository): EventRepository
}