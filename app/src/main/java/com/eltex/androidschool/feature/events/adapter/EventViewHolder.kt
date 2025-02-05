package com.eltex.androidschool.feature.events.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.feature.events.ui.EventUiModel
import com.eltex.androidschool.feature.events.data.Type

class EventViewHolder(
    private val binding: CardEventBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(payload: EventPayload) {
        if (payload.like != null && payload.likes != null) {
            updateLike(payload.like, payload.likes)
        }
        if (payload.participants != null && payload.participate != null) {
            updateParticipate(payload.participate, payload.participants)
        }
    }

    fun bind(event: EventUiModel) {
        binding.content.text = event.content
        updateLike(event.likedByMe, event.likes)
        updateParticipate(event.participatedByMe, event.participants)
        binding.author.text = event.author
        binding.published.text = event.published
        binding.datetime.text = event.datetime
        binding.avatarInitials.text = event.author.take(1)
        binding.link.text = event.link
        binding.type.setText(
            when (event.type) {
                Type.OFFLINE -> R.string.offline
                Type.ONLINE -> R.string.online
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateLike(likedByMe: Boolean, likes: Int) {
        binding.like.setIconResource(
            if (likedByMe) {
                R.drawable.liked_icon
            } else {
                R.drawable.like_icon_border
            }
        )
        binding.like.text = likes.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun updateParticipate(participatedByMe: Boolean, participants: Int) {
        binding.participate.setIconResource(
            if (participatedByMe){
                R.drawable.baseline_people_24
            } else {
                R.drawable.people_outline
            }
        )
        binding.participate.text = participants.toString()
    }
}
