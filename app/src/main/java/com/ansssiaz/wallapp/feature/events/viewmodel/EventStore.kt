package com.ansssiaz.wallapp.feature.events.viewmodel

import com.ansssiaz.wallapp.mvi.Store

typealias EventStore = Store<EventUiState, EventMessage, EventEffect>