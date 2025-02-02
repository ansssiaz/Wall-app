package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.MainDispatcherRule
import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class PostViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `delete error then error state`() {
        val error = RuntimeException("Test error")

        val viewModel = PostViewModel(
            repository = object : PostRepository {
                override suspend fun getPosts(): List<Post> = emptyList()
                override suspend fun like(id: Long): Post = error("Not implemented")
                override suspend fun savePost(id: Long, content: String): Post = error("Not implemented")
                override suspend fun delete(id: Long) = throw (error)
                override suspend fun deleteLike(id: Long): Post = error(("Not implemented"))
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
                override suspend fun getPosts(): List<Post> = emptyList()
                override suspend fun like(id: Long): Post = error("Not implemented")
                override suspend fun savePost(id: Long, content: String): Post = error("Not implemented")
                override suspend fun delete(id: Long) {}
                override suspend fun deleteLike(id: Long): Post = error("Not implemented")
            },
            mapper = PostUiModelMapper(),
        )
        viewModel.deleteById(1)
        assertTrue(viewModel.uiState.value.status is Status.Idle)
    }
}
