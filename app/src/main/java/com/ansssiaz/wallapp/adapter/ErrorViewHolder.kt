package com.ansssiaz.wallapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.ansssiaz.wallapp.databinding.ItemErrorBinding
import com.ansssiaz.wallapp.utils.getErrorText

class ErrorViewHolder(
    private val binding: ItemErrorBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(throwable: Throwable) {
        binding.errorText.text = throwable.getErrorText(binding.root.context)
    }
}