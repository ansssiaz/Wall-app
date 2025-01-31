package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.SchedulersFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EventViewModel(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper,
    private val schedulersFactory: SchedulersFactory = SchedulersFactory.DEFAULT,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val disposable = CompositeDisposable()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }

        repository.getEvents()
            .observeOn(schedulersFactory.computation())
            .map { events ->
                events.map {
                    mapper.map(it)
                }
            }
            .observeOn(schedulersFactory.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(events = data, status = Status.Idle)
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

    fun like(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }
        if (!event.likedByMe) {
            repository.like(event.id)
                .map {
                    mapper.map(it)
                }
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
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
            repository.deleteLike(event.id)
                .map {
                    mapper.map(it)
                }
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
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

    fun participate(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }
        if (!event.participatedByMe) {
            repository.participate(event.id)
                .map {
                    mapper.map(it)
                }
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
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
            repository.deleteParticipation(event.id)
                .map {
                    mapper.map(it)
                }
                .subscribeBy(
                    onSuccess = { data ->
                        _uiState.update { state ->
                            state.copy(
                                events = state.events.orEmpty().map {
                                    if (it.id == event.id) {
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
                            events = state.events.orEmpty().filter { it.id != id },
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

    override fun onCleared() {
        disposable.dispose()
    }
}
