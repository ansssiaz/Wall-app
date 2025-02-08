package com.eltex.androidschool.feature.posts.repository.di

import com.eltex.androidschool.feature.posts.repository.NetworkPostRepository
import com.eltex.androidschool.feature.posts.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface PostRepositoryModule {
    @Binds
    fun bindNetworkPostRepository(impl: NetworkPostRepository): PostRepository
}