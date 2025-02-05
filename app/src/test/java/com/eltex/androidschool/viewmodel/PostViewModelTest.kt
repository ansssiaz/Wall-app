package com.eltex.androidschool.viewmodel

class PostViewModelTest {
    /*@get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `delete error then error state`() {
        val error = RuntimeException("Test error")

        val viewModel = PostViewModel(
            repository = object : PostRepository {
                override suspend fun getPosts(): List<com.eltex.androidschool.feature.posts.data.Post> = emptyList()
                override suspend fun like(id: Long): com.eltex.androidschool.feature.posts.data.Post = error("Not implemented")
                override suspend fun savePost(id: Long, content: String): com.eltex.androidschool.feature.posts.data.Post = error("Not implemented")
                override suspend fun delete(id: Long) = throw (error)
                override suspend fun deleteLike(id: Long): com.eltex.androidschool.feature.posts.data.Post = error(("Not implemented"))
            },
            mapper = PostUiModelMapper(),
        )
        viewModel.deleteById(1)
        assertEquals(error, (viewModel.uiState.value.status as Status.Error).throwable)
    }

    @Test
    fun `delete success then success state`() {
        val viewModel = PostViewModel(
            repository = object : PostRepository {
                override suspend fun getPosts(): List<com.eltex.androidschool.feature.posts.data.Post> = emptyList()
                override suspend fun like(id: Long): com.eltex.androidschool.feature.posts.data.Post = error("Not implemented")
                override suspend fun savePost(id: Long, content: String): com.eltex.androidschool.feature.posts.data.Post = error("Not implemented")
                override suspend fun delete(id: Long) {}
                override suspend fun deleteLike(id: Long): com.eltex.androidschool.feature.posts.data.Post = error("Not implemented")
            },
            mapper = PostUiModelMapper(),
        )
        viewModel.deleteById(1)
        assertTrue(viewModel.uiState.value.status is Status.Idle)
    }*/
}
