package com.ansssiaz.wallapp.utils

sealed interface Status {
    val throwableOrNull: Throwable?
        get() = (this as? Error)?.throwable

    data object Idle : Status
    data object Loading : Status
    data class Error(val throwable: Throwable) : Status
}