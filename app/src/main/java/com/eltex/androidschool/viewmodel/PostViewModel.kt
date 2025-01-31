package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.SchedulersFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PostViewModel(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper,
    private val schedulersFactory: SchedulersFactory = SchedulersFactory.DEFAULT,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    private val disposable = CompositeDisposable()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }

        repository.getPosts()
            .observeOn(schedulersFactory.computation())
            .map { posts ->
                posts.map {
                    mapper.map(it)
                }
            }
            .observeOn(schedulersFactory.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(posts = data, status = Status.Idle)
                    }
                },
                onError = { exception ->
                    _uiState.update {
                        it.copy(status = Status.Error(exception))
                    }
                }

            )
            .addTo(disposable)
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
        if (!post.likedByMe) {
            repository.like(post.id)
                .map {
                    mapper.map(it)
                }
                .subscribeBy(
                    onSuccess = { data ->
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
                    },
                    onError = { exception ->
                        _uiState.update {
                            it.copy(status = Status.Error(exception))
                        }
                    }
                )
                .addTo(disposable)
        } else {
            repository.deleteLike(post.id)
                .map {
                    mapper.map(it)
                }
                .subscribeBy(
                    onSuccess = { data ->
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
                    },
                    onError = { exception ->
                        _uiState.update {
                            it.copy(status = Status.Error(exception))
                        }
                    }
                )
                .addTo(disposable)
        }
    }

    fun deleteById(id: Long) {
        _uiState.update { it.copy(status = Status.Loading) }
        repository.delete(id)
            .subscribeBy(
                onComplete = {
                    _uiState.update { state ->
                        state.copy(
                            posts = state.posts.orEmpty().filter { it.id != id },
                            status = Status.Idle,
                        )
                    }
                },
                onError = { exception ->
                    _uiState.update {
                        it.copy(status = Status.Error(exception))
                    }
                }
            )
            .addTo(disposable)
    }
}
