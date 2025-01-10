package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

class EventsAdapter(
    private val listener: EventListener,
) : ListAdapter<Event, EventViewHolder>(EventDiffCallback()) {

    interface EventListener {
        fun onLikeClicked(event: Event)
        fun onParticipateClicked(event: Event)
        fun onShareClicked(event: Event)
        fun onDeleteClicked(event: Event)
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
                inflate(R.menu.event_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            listener.onDeleteClicked(getItem(viewHolder.adapterPosition))
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