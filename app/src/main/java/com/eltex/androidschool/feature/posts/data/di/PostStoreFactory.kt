package com.eltex.androidschool.feature.posts.data.di

import com.eltex.androidschool.feature.posts.effecthandler.PostEffectHandler
import com.eltex.androidschool.feature.posts.reducer.PostReducer
import com.eltex.androidschool.feature.posts.repository.PostRepository
import com.eltex.androidschool.feature.posts.ui.PostUiModelMapper
import com.eltex.androidschool.feature.posts.viewmodel.PostMessage
import com.eltex.androidschool.feature.posts.viewmodel.PostStore
import com.eltex.androidschool.feature.posts.viewmodel.PostUiState

class PostStoreFactory(private val repository: PostRepository) {
    fun create(): PostStore = PostStore(
        PostReducer(),
        PostEffectHandler(
            repository,
            PostUiModelMapper()
        ),
        setOf(PostMessage.Refresh),
        PostUiState(),
    )

}