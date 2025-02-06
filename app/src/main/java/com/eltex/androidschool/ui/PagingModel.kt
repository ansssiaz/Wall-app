package com.eltex.androidschool.ui

sealed interface PagingModel<out T> {
    data class Data<T>(val value: T) : PagingModel<T>
    data class Error(val reason: Throwable) : PagingModel<Nothing>
    data object PostSkeleton: PagingModel<Nothing>
    data object EventSkeleton: PagingModel<Nothing>
}