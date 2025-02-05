package com.eltex.androidschool.feature.events.fragment

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
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.feature.events.adapter.EventsAdapter
import com.eltex.androidschool.feature.events.api.EventsApi
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.feature.events.effecthandler.EventEffectHandler
import com.eltex.androidschool.feature.events.reducer.EventReducer
import com.eltex.androidschool.feature.newevent.fragment.NewEventFragment
import com.eltex.androidschool.itemdecoration.EventDateDecoration
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.feature.events.ui.EventUiModelMapper
import com.eltex.androidschool.feature.events.ui.EventUiModel
import com.eltex.androidschool.feature.events.repository.NetworkEventRepository
import com.eltex.androidschool.feature.events.viewmodel.EventMessage
import com.eltex.androidschool.feature.events.viewmodel.EventStore
import com.eltex.androidschool.feature.events.viewmodel.EventUiState
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.feature.events.viewmodel.EventViewModel
import com.eltex.androidschool.feature.events.viewmodel.EventStatus.*
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventsFragment : Fragment() {
    private var isLoading = false
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
                        EventStore(
                            EventReducer(),
                            EventEffectHandler(
                                NetworkEventRepository(EventsApi.INSTANCE),
                                EventUiModelMapper()
                            ),
                            setOf(EventMessage.Refresh),
                            EventUiState(),
                        )
                    )
                }
            }
        }
        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClicked(event: EventUiModel) {
                    viewModel.accept(EventMessage.Like(event))
                }

                override fun onParticipateClicked(event: EventUiModel) {
                    viewModel.accept(EventMessage.Participate(event))
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
                    viewModel.accept(EventMessage.Delete(event))
                }

                override fun onEditClicked(event: EventUiModel) {
                    requireParentFragment().requireParentFragment().findNavController().navigate(
                        R.id.action_bottomMenuFragment_to_newEventFragment,
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
                    if (position in events.indices) {
                        viewModel.uiState.value.events[position]
                    } else {
                        null
                    }
                },
                context = this.requireContext()
            )
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(EventMessage.Refresh)
        }

        binding.retryButton.setOnClickListener {
            viewModel.accept(EventMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewEventFragment.EVENT_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(EventMessage.Refresh)
        }

        binding.list.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    val itemsCount = adapter.itemCount
                    val adapterPosition = binding.list.getChildAdapterPosition(view)

                    //Если пользователь доскроллил до середины списка, и данные сейчас не загружаются - загружаем следующую страницу
                    if (adapterPosition >= itemsCount / 2 && adapterPosition < itemsCount - 2 && !isLoading) {
                        isLoading = true
                        viewModel.accept(EventMessage.LoadNextPage)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) = Unit
            }
        )

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                isLoading = when (it.status) {
                    NextPageLoading -> {
                        true
                    }

                    Idle, EmptyLoading, Refreshing, is EmptyError, is NextPageError -> {
                        false
                    }
                }

                binding.swipeRefresh.isRefreshing = it.isRefreshing

                binding.errorGroup.isVisible = it.isEmptyError
                val errorText = it.emptyError?.getErrorText(requireContext())
                binding.errorText.text = errorText

                binding.progress.isVisible = it.isEmptyLoading

                if (it.singleError != null) {
                    val singleErrorText = it.singleError.getErrorText(requireContext())
                    Toast.makeText(requireContext(), singleErrorText, Toast.LENGTH_SHORT).show()
                    viewModel.accept(EventMessage.HandleError)
                }

                adapter.submitList(it.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}
