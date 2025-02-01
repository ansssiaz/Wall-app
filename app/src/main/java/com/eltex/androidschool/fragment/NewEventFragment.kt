package com.eltex.androidschool.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.mapper.PostUiModelMapper.Companion.FORMATTER
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.viewmodel.NewEventViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId.systemDefault

class NewEventFragment : Fragment() {
    companion object {
        const val ARG_EVENT_ID = "ARG_EVENT_ID"
        const val ARG_CONTENT = "ARG_CONTENT"
        const val ARG_DATETIME = "ARG_DATETIME"
        const val EVENT_CREATED_RESULT = "EVENT_CREATED_RESULT"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    override fun onStart() {
        super.onStart()
        toolbarViewModel.setSaveVisibility(true)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.setSaveVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        toolbarViewModel.setTitle(getString(R.string.app_name))
    }

    override fun onCreateView(
        inflater:
        LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentNewEventBinding.inflate(layoutInflater)

        val id = arguments?.getLong(ARG_EVENT_ID) ?: 0L

        val initialContent = arguments?.getString(ARG_CONTENT) ?: ""
        binding.content.setText(initialContent)

        var datetime =
            if (id != 0L) // Если не выбраны дата и время события, то по умолчанию установим указанные раньше (при редактировании) или текущие (при создании)
                LocalDateTime.parse(arguments?.getString(ARG_DATETIME), FORMATTER)
                    .atZone(systemDefault()).toInstant()
            else Instant.now()

        var formattedDatetime = FORMATTER.format(LocalDateTime.now().atZone(systemDefault()))

        binding.editTextDate.setText(arguments?.getString(ARG_DATETIME) ?: formattedDatetime)

        val title =
            if (id != 0L) R.string.edit_event_title else R.string.new_event_title

        var datePicker: MaterialDatePicker<Long>? = null
        var timePicker: MaterialTimePicker? = null
        var selectedDate: LocalDate?

        fun setupDatePicker() {
            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.date_picker_title))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker?.addOnPositiveButtonClickListener { selection ->
                selectedDate = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000))

                if (timePicker == null) {
                    timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .build()

                    timePicker?.addOnPositiveButtonClickListener {
                        val selectedHour = timePicker?.hour ?: 12
                        val selectedMinute = timePicker?.minute ?: 0

                        val selectedTime = LocalTime.of(selectedHour, selectedMinute)
                        datetime =
                            selectedDate?.atTime(selectedTime)?.atZone(systemDefault())?.toInstant()

                        formattedDatetime = FORMATTER.format(datetime?.atZone(systemDefault()))
                        binding.editTextDate.setText(formattedDatetime)
                    }
                }

                timePicker?.show(parentFragmentManager, "TIME_PICKER")
            }
        }

        binding.buttonSelectDate.setOnClickListener {
            if (datePicker == null) {
                setupDatePicker() // Настраиваем datePicker, если еще не настроен
            }
            datePicker?.show(parentFragmentManager, "DATE_PICKER")
        }

        val viewModel by viewModels<NewEventViewModel> {
            viewModelFactory {
                initializer {
                    NewEventViewModel(
                        repository = NetworkEventRepository(EventsApi.INSTANCE),
                        id = id,
                    )
                }
            }
        }

        val toolbarViewModel by activityViewModels<ToolbarViewModel>()
        toolbarViewModel.setTitle(getString(title))
        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()
                if (content.isBlank()) {
                    Toast.makeText(requireContext(), R.string.empty_event_error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addEvent(content, datetime)
                }
                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.event != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        EVENT_CREATED_RESULT,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                }

                it.status.throwableOrNull?.getErrorText(requireContext())?.let { errorText ->
                    Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
                    viewModel.consumeError()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}