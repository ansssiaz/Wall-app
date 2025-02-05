package com.eltex.androidschool.feature.events.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.feature.events.ui.EventUiModel

class EventDiffCallback : DiffUtil.ItemCallback<EventUiModel>() {
    override fun areItemsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: EventUiModel, newItem: EventUiModel): Any? =
        EventPayload(
            like = newItem.likedByMe.takeIf {
                it != oldItem.likedByMe
            },
            participate = newItem.participatedByMe.takeIf {
                it != oldItem.participatedByMe
            },
            likes = newItem.likes.takeIf {
                it != oldItem.likes
            },
            participants = newItem.participants.takeIf {
                it != oldItem.participants
            },
        )
            .takeIf {
                it.isNotEmpty()
            }
}