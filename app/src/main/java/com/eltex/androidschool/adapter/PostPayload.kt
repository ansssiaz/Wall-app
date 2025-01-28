package com.eltex.androidschool.adapter

data class PostPayload(
    val like: Boolean? = null,
) {
    fun isNotEmpty(): Boolean = (like != null)
}