package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.model.Event

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: Event, newItem: Event): Any? =
        EventPayload(
            like = newItem.likedByMe.takeIf {
                it != oldItem.likedByMe
            },
            participate = newItem.participatedByMe.takeIf {
                it != oldItem.participatedByMe
            },
        )
            .takeIf {
                it.isNotEmpty()
            }
}