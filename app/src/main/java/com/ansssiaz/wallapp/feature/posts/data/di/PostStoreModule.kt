package com.ansssiaz.wallapp.feature.posts.data.di

import com.ansssiaz.wallapp.feature.posts.effecthandler.PostEffectHandler
import com.ansssiaz.wallapp.feature.posts.reducer.PostReducer
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostMessage
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostStore
import com.ansssiaz.wallapp.feature.posts.viewmodel.PostUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class PostStoreModule {
    @Provides
    fun create(
        reducer: PostReducer,
        effectHandler: PostEffectHandler
    ): PostStore = PostStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(PostMessage.Refresh),
        initState = PostUiState(),
    )

}