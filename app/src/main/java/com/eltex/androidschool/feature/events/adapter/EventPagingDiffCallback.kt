package com.eltex.androidschool.feature.events.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.feature.events.ui.EventPagingModel
import com.eltex.androidschool.ui.PagingModel

class EventPagingDiffCallback : DiffUtil.ItemCallback<EventPagingModel>() {
    private val delegate = EventDiffCallback()

    override fun areItemsTheSame(oldItem: EventPagingModel, newItem: EventPagingModel): Boolean {
        if (oldItem::class != newItem::class) return false
        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.areItemsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: EventPagingModel, newItem: EventPagingModel): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: EventPagingModel, newItem: EventPagingModel): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }
        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.getChangePayload(oldItem.value, newItem.value)
        } else {
            null
        }
    }
}