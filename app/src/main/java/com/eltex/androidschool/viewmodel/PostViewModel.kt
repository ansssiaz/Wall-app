package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }

        repository.getPosts(
            object : Callback<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _uiState.update {
                        it.copy(posts = data, status = Status.Idle)
                    }
                }

                override fun onError(exception: Throwable) {
                    _uiState.update {
                        it.copy(status = Status.Error(exception))
                    }
                }

            }
        )
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

    fun like(post: Post) {
        _uiState.update { it.copy(status = Status.Loading) }
        if (!post.likedByMe) {
            repository.like(post.id, object : Callback<Post> {
                override fun onSuccess(data: Post) {
                    _uiState.update { state ->
                        state.copy(
                            posts = state.posts.orEmpty().map {
                                if (it.id == post.id) {
                                    data
                                } else {
                                    it
                                }
                            },
                            status = Status.Idle,
                        )
                    }
                }
                override fun onError(exception: Throwable) {
                    _uiState.update {
                        it.copy(status = Status.Error(exception))
                    }
                }
            })
        } else {
            repository.deleteLike(post.id, object : Callback<Post> {
                override fun onSuccess(data: Post) {
                    _uiState.update { state ->
                        state.copy(
                            posts = state.posts.orEmpty().map {
                                if (it.id == post.id) {
                                    data
                                } else {
                                    it
                                }
                            },
                            status = Status.Idle,
                        )
                    }
                }

                override fun onError(exception: Throwable) {
                    _uiState.update {
                        it.copy(status = Status.Error(exception))
                    }
                }
            })
        }
    }

    fun deleteById(id: Long) {
        _uiState.update { it.copy(status = Status.Loading) }
        repository.delete(id, object : Callback<Unit> {
            override fun onSuccess(data: Unit) {
                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.orEmpty().filter { it.id != id },
                        status = Status.Idle,
                    )
                }
            }
            override fun onError(exception: Throwable) {
                _uiState.update {
                    it.copy(status = Status.Error(exception))
                }
            }
        })
    }
}
