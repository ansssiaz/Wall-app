package com.eltex.androidschool.feature.posts.adapter

data class PostPayload(
    val like: Boolean? = null,
    val likes: Int? = 0
) {
    fun isNotEmpty(): Boolean = (like != null) || (likes != null)
}