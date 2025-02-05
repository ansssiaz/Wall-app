package com.eltex.androidschool.viewmodel

class EventViewModelTest {
   /* @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `delete error then error state`() {
        val error = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun like(id: Long): Event = error("Not implemented")
                override suspend fun participate(id: Long): Event = error("Not implemented")
                override suspend fun saveEvent(id: Long, content: String, datetime: Instant): Event = error("Not implemented")
                override suspend fun delete(id: Long) { throw (error) }
                override suspend fun deleteLike(id: Long): Event = error("Not implemented")
                override suspend fun deleteParticipation(id: Long): Event = error("Not implemented")
            },
            mapper = EventUiModelMapper(),
        )
        viewModel.deleteById(1)
        assertEquals(error, (viewModel.uiState.value.status as Status.Error).throwable)
    }

    @Test
    fun `delete success then success state`() {
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()
                override suspend fun like(id: Long): Event = error("Not implemented")
                override suspend fun participate(id: Long): Event = error("Not implemented")
                override suspend fun saveEvent(id: Long, content: String, datetime: Instant): Event = error("Not implemented")
                override suspend fun delete(id: Long) {}
                override suspend fun deleteLike(id: Long): Event = error("Not implemented")
                override suspend fun deleteParticipation(id: Long): Event = error("Not implemented")
            },
            mapper = EventUiModelMapper(),
        )
        viewModel.deleteById(1)
        assertTrue(viewModel.uiState.value.status is Status.Idle)
    }*/
}
