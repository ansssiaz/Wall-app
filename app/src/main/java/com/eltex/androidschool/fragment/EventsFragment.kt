package com.eltex.androidschool.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eltex.androidschool.R
import android.content.Intent
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.itemdecoration.EventDateDecoration
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.getErrorText
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
                    EventViewModel(NetworkEventRepository(EventsApi.INSTANCE), EventUiModelMapper())
                }
            }
        }
        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClicked(event: EventUiModel) {
                    viewModel.like(event)
                }

                override fun onParticipateClicked(event: EventUiModel) {
                    viewModel.participate(event)
                }

                override fun onShareClicked(event: EventUiModel) {
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

                override fun onDeleteClicked(event: EventUiModel) {
                    viewModel.deleteById(event.id)
                }

                override fun onEditClicked(event: EventUiModel) {
                    findNavController().navigate(
                        R.id.action_eventsFragment_to_newEventFragment,
                        bundleOf(
                            NewEventFragment.ARG_EVENT_ID to event.id,
                            NewEventFragment.ARG_CONTENT to event.content,
                            NewEventFragment.ARG_DATETIME to event.datetime
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
            EventDateDecoration(
                getEvent = { position ->
                    val events = viewModel.uiState.value.events
                    if (events != null && position in events.indices) {
                        viewModel.uiState.value.events?.get(position)
                    } else {
                        null
                    }
                },
                context = this.requireContext()
            )
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
        }

        binding.retryButton.setOnClickListener {
            viewModel.load()
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewEventFragment.EVENT_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.load()
        }

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.swipeRefresh.isRefreshing = it.isRefreshing

                binding.errorGroup.isVisible = it.isEmptyError
                val errorText = it.status.throwableOrNull?.getErrorText(requireContext())
                binding.errorText.text = errorText

                binding.progress.isVisible = it.isEmptyLoading

                if (it.isRefreshError) {
                    Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
                    viewModel.consumeError()
                }

                adapter.submitList(it.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}
