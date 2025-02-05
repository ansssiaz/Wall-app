package com.eltex.androidschool.ui

sealed interface PagingModel<out T> {
    data class Data<T>(val value: T) : PagingModel<T>
    data object Loading : PagingModel<Nothing>
    data class Error(val reason: Throwable) : PagingModel<Nothing>
}