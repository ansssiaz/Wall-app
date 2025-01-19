package com.eltex.androidschool.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eltex.androidschool.R
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.db.AppDb
import com.eltex.androidschool.itemdecoration.DateDecoration
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.SQLiteEventRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)
        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(
                        SQLiteEventRepository(
                            AppDb.getInstance(requireContext().applicationContext).eventDao
                        )
                    )
                }
            }
        }
        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClicked(event: Event) {
                    viewModel.likeById(event.id)
                }

                override fun onParticipateClicked(event: Event) {
                    viewModel.participate(event.id)
                }

                override fun onShareClicked(event: Event) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(
                            Intent.EXTRA_TEXT,
                            getString(R.string.share_text, event.author, event.content)
                        )
                        .setType("text/plain")
                    val chooser = Intent.createChooser(intent, null)
                    startActivity(chooser)
                }

                override fun onDeleteClicked(event: Event) {
                    viewModel.deleteById(event.id)
                }

                override fun onEditClicked(event: Event) {
                    findNavController().navigate(
                        R.id.action_eventsFragment_to_newEventFragment,
                        bundleOf(
                            NewEventFragment.ARG_EVENT_ID to event.id,
                            NewEventFragment.ARG_CONTENT to event.content,
                        )
                    )
                }
            }
        )
        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )
        binding.list.addItemDecoration(
            DateDecoration(
                getEvent = { position ->
                    if (position in 0 until viewModel.uiState.value.events.size) {
                        viewModel.uiState.value.events[position]
                    } else {
                        null
                    }
                },
                context = this.requireContext()
            )
        )
        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                adapter.submitList(it.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }
}
