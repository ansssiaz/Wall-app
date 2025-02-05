package com.eltex.androidschool.feature.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.feature.events.ui.EventUiModel

class EventsAdapter(
    private val listener: EventListener,
) : ListAdapter<EventUiModel, EventViewHolder>(EventDiffCallback()) {

    interface EventListener {
        fun onLikeClicked(event: EventUiModel)
        fun onParticipateClicked(event: EventUiModel)
        fun onShareClicked(event: EventUiModel)
        fun onDeleteClicked(event: EventUiModel)
        fun onEditClicked(event: EventUiModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EventViewHolder(binding)

        binding.like.setOnClickListener {
            listener.onLikeClicked(getItem(viewHolder.adapterPosition))
        }
        binding.participate.setOnClickListener {
            listener.onParticipateClicked(getItem(viewHolder.adapterPosition))
        }
        binding.share.setOnClickListener {
            listener.onShareClicked(getItem(viewHolder.adapterPosition))
        }
        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.action_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            listener.onDeleteClicked(getItem(viewHolder.adapterPosition))
                            true
                        }

                        R.id.edit -> {
                            listener.onEditClicked(getItem(viewHolder.adapterPosition))
                            true
                        }

                        else -> false
                    }
                }
                show()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int,
        payloads: List<Any>,
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is EventPayload) {
                    holder.bind(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}