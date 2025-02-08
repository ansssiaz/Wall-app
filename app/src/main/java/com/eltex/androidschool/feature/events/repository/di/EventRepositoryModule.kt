package com.eltex.androidschool.feature.events.repository.di

import com.eltex.androidschool.feature.events.repository.EventRepository
import com.eltex.androidschool.feature.events.repository.NetworkEventRepository
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