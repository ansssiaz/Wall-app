package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PostViewModel(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val posts: List<PostUiModel> = repository.getPosts()
                    .map {
                        mapper.map(it)
                    }

                _uiState.update {
                    it.copy(posts = posts, status = Status.Idle)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }

        }
    }

    fun consumeError() {
        _uiState.update {
            if (it.status is Status.Error) {
                it.copy(status = Status.Idle)
            } else {
                it
            }
        }
    }

    fun like(post: PostUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val likedPost =
                    if (!post.likedByMe) repository.like(post.id) else repository.deleteLike(post.id)

                val likedPostUiModel = mapper.map(likedPost)

                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.orEmpty().map {
                            if (it.id == post.id) {
                                likedPostUiModel
                            } else {
                                it
                            }
                        },
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun deleteById(id: Long) {
        _uiState.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                repository.delete(id)

                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.orEmpty().filter { it.id != id },
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }
}