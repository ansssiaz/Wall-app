package com.ansssiaz.wallapp.feature.events.adapter

data class EventPayload(
    val like: Boolean? = null,
    val participate: Boolean? = null,
    val likes: Int? = null,
    val participants: Int? = null
) {
    fun isNotEmpty(): Boolean = (like != null) || (participate != null) || (likes != null) || (participants != null)
}