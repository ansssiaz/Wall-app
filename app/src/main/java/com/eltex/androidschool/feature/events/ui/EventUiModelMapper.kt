package com.eltex.androidschool.feature.events.ui

import com.eltex.androidschool.feature.posts.ui.PostUiModelMapper.Companion.FORMATTER
import com.eltex.androidschool.feature.events.data.Event
import org.threeten.bp.ZoneId.systemDefault

class EventUiModelMapper {
    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            content = content,
            published = FORMATTER.format(published.atZone(systemDefault())),
            datetime = FORMATTER.format(datetime.atZone(systemDefault())),
            type = type,
            likedByMe = likedByMe,
            participatedByMe = participatedByMe,
            link = link,
            likes =likeOwnerIds.size,
            participants = participantsIds.size
        )
    }
}