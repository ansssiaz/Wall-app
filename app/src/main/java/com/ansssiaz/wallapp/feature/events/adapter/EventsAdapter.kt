package com.ansssiaz.wallapp.feature.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ansssiaz.wallapp.R
import com.ansssiaz.wallapp.adapter.ErrorViewHolder
import com.ansssiaz.wallapp.databinding.CardEventBinding
import com.ansssiaz.wallapp.databinding.EventSkeletonBinding
import com.ansssiaz.wallapp.databinding.ItemErrorBinding
import com.ansssiaz.wallapp.feature.events.ui.EventPagingModel
import com.ansssiaz.wallapp.feature.events.ui.EventUiModel
import com.ansssiaz.wallapp.ui.PagingModel

class EventsAdapter(
    private val listener: EventListener,
) : ListAdapter<EventPagingModel, RecyclerView.ViewHolder>(EventPagingDiffCallback()) {

    interface EventListener {
        fun onLikeClicked(event: EventUiModel)
        fun onParticipateClicked(event: EventUiModel)
        fun onShareClicked(event: EventUiModel)
        fun onDeleteClicked(event: EventUiModel)
        fun onEditClicked(event: EventUiModel)
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is PagingModel.Data -> R.layout.card_event
        is PagingModel.Error -> R.layout.item_error
        PagingModel.EventSkeleton -> R.layout.event_skeleton
        PagingModel.PostSkeleton -> 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.card_event -> createEventViewHolder(parent)
            R.layout.item_error -> createErrorViewHolder(parent)
            R.layout.event_skeleton -> createEventSkeletonViewHolder(parent)
            else -> error("Unknown viewType: $viewType")
        }
    }

    private fun createEventSkeletonViewHolder(parent: ViewGroup): EventSkeletonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventSkeletonBinding.inflate(layoutInflater, parent, false)
        return EventSkeletonViewHolder(binding)
    }

    private fun createErrorViewHolder(parent: ViewGroup): ErrorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemErrorBinding.inflate(layoutInflater, parent, false)
        return ErrorViewHolder(binding)
    }

    private fun createEventViewHolder(parent: ViewGroup): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EventViewHolder(binding)

        binding.like.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
            item?.value?.let(listener::onLikeClicked)
        }
        binding.participate.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
            item?.value?.let(listener::onParticipateClicked)
        }
        binding.share.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
            item?.value?.let(listener::onShareClicked)
        }
        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.action_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.delete -> {
                            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
                            item?.value?.let(listener::onDeleteClicked)
                            true
                        }

                        R.id.edit -> {
                            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
                            item?.value?.let(listener::onEditClicked)
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
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>,
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is EventPayload) {
                    (holder as? EventViewHolder)?.bind(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagingModel.Data -> (holder as EventViewHolder).bind(item.value)
            is PagingModel.Error -> (holder as ErrorViewHolder).bind(item.reason)
            else -> Unit
        }
    }
}