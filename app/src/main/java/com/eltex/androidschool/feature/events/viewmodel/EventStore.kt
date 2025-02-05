package com.eltex.androidschool.feature.events.viewmodel

import com.eltex.androidschool.mvi.Store

typealias EventStore = Store<EventUiState, EventMessage, EventEffect>