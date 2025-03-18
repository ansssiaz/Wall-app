package com.ansssiaz.wallapp.feature.events.ui

import com.ansssiaz.wallapp.feature.events.data.Event
import com.ansssiaz.wallapp.utils.DateFormatter
import javax.inject.Inject

class EventUiModelMapper @Inject constructor(private val dateFormatter: DateFormatter) {
    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            content = content,
            published = dateFormatter.formatDate(published),
            datetime = dateFormatter.formatDate(datetime),
            type = type,
            likedByMe = likedByMe,
            participatedByMe = participatedByMe,
            link = link,
            likes = likeOwnerIds.size,
            participants = participantsIds.size
        )
    }
}