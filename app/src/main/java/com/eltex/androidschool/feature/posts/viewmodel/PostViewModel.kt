package com.eltex.androidschool.feature.posts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PostViewModel(
    private val store: PostStore,
) : ViewModel() {
    val uiState: StateFlow<PostUiState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: PostMessage) {
        store.accept(message)
    }
}