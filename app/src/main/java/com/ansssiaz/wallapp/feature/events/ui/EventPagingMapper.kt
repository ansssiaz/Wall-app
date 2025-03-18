package com.ansssiaz.wallapp.feature.events.ui

import com.ansssiaz.wallapp.feature.events.viewmodel.EventStatus
import com.ansssiaz.wallapp.feature.events.viewmodel.EventUiState
import com.ansssiaz.wallapp.ui.PagingModel

object EventPagingMapper {
    private const val LOADING_ITEM_COUNT = 10
    fun map(state: EventUiState): List<PagingModel<EventUiModel>> {
        val events = state.events.map {
            PagingModel.Data(it)
        }
        return when (val statusValue = state.status) {
            is EventStatus.NextPageError -> events + PagingModel.Error(statusValue.reason)

            EventStatus.NextPageLoading -> {
                val loadingSkeletons = List(LOADING_ITEM_COUNT) { PagingModel.EventSkeleton }
                events + loadingSkeletons
            }

            EventStatus.EmptyLoading -> List(LOADING_ITEM_COUNT) { PagingModel.EventSkeleton }

            is EventStatus.Idle,
            EventStatus.Refreshing,
            is EventStatus.EmptyError -> events
        }
    }
}