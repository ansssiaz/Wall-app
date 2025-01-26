package com.eltex.androidschool.utils

interface Callback<T> {
    fun onSuccess(data: T)
    fun onError(exception: Exception)
}