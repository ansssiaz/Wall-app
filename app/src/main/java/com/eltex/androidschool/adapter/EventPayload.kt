package com.eltex.androidschool.adapter

data class EventPayload(
    val like: Boolean? = null,
    val participate: Boolean? = null,
) {
    fun isNotEmpty(): Boolean = (like != null) || (participate != null)
}