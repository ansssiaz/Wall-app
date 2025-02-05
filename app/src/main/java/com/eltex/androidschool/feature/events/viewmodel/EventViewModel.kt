package com.eltex.androidschool.feature.events.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EventViewModel(
    private val store: EventStore,
) : ViewModel() {
    val uiState: StateFlow<EventUiState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: EventMessage) {
        store.accept(message)
    }
}
