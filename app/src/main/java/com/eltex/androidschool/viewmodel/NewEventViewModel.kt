package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long = 0,
) : ViewModel() {
    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()

    private val disposable = CompositeDisposable()

    fun addEvent(content: String) {
        _state.update { it.copy(status = Status.Loading) }
        repository.saveEvent(id, content)
            .subscribeBy(
                onSuccess = { data ->
                    _state.update { it.copy(event = data, status = Status.Idle) }
                },
                onError = { exception ->
                    _state.update { it.copy(status = Status.Error(exception)) }
                }
            )
            .addTo(disposable)
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }

    override fun onCleared() {
        disposable.dispose()
    }
}