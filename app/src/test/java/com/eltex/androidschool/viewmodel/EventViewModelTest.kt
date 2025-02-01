package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.TestSchedulersFactory
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.threeten.bp.Instant

class EventViewModelTest {
    @Test
    fun `delete error then error state`() {
        val error = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())
                override fun like(id: Long): Single<Event> = Single.never()
                override fun participate(id: Long): Single<Event> = Single.never()
                override fun saveEvent(
                    id: Long,
                    content: String,
                    datetime: Instant
                ): Single<Event> =
                    Single.error(RuntimeException())

                override fun delete(id: Long): Completable = Completable.error(error)
                override fun deleteLike(id: Long): Single<Event> = Single.never()
                override fun deleteParticipation(id: Long): Single<Event> = Single.never()
            },
            mapper = EventUiModelMapper(),
            schedulersFactory = TestSchedulersFactory,
        )
        viewModel.deleteById(1)
        assertEquals(error, (viewModel.uiState.value.status as Status.Error).throwable)
    }

    @Test
    fun `delete success then success state`() {
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())
                override fun like(id: Long): Single<Event> = Single.never()
                override fun participate(id: Long): Single<Event> = Single.never()
                override fun saveEvent(
                    id: Long,
                    content: String,
                    datetime: Instant
                ): Single<Event> =
                    Single.error(RuntimeException())

                override fun delete(id: Long): Completable =
                    Completable.complete()

                override fun deleteLike(id: Long): Single<Event> = Single.never()
                override fun deleteParticipation(id: Long): Single<Event> = Single.never()
            },
            mapper = EventUiModelMapper(),
            schedulersFactory = TestSchedulersFactory,
        )

        viewModel.deleteById(1)
        assertTrue(viewModel.uiState.value.status is Status.Idle)
    }
}
