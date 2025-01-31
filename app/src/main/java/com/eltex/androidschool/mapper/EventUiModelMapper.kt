package com.eltex.androidschool.mapper

import com.eltex.androidschool.mapper.PostUiModelMapper.Companion.FORMATTER
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
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