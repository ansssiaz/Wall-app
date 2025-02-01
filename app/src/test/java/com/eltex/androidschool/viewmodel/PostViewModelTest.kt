package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.TestSchedulersFactory
import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PostViewModelTest {
    @Test
    fun `delete error then error state`() {
        val error = RuntimeException("Test error")
        val viewModel = PostViewModel(
            repository = object : PostRepository {
                override fun getPosts(): Single<List<Post>> = Single.just(emptyList())
                override fun like(id: Long): Single<Post> = Single.never()
                override fun savePost(id: Long, content: String): Single<Post> =
                    Single.error(RuntimeException())

                override fun delete(id: Long): Completable = Completable.error(error)
                override fun deleteLike(id: Long): Single<Post> = Single.never()
            },
            mapper = PostUiModelMapper(),
            schedulersFactory = TestSchedulersFactory,
        )
        viewModel.deleteById(1)
        assertEquals(error, (viewModel.uiState.value.status as Status.Error).throwable)
    }

    @Test
    fun `delete success then success state`() {
        val viewModel = PostViewModel(
            repository = object : PostRepository {
                override fun getPosts(): Single<List<Post>> = Single.just(emptyList())
                override fun like(id: Long): Single<Post> = Single.never()
                override fun savePost(id: Long, content: String, ): Single<Post> = Single.error(RuntimeException())
                override fun delete(id: Long): Completable = Completable.complete()
                override fun deleteLike(id: Long): Single<Post> = Single.never()
            },
            mapper = PostUiModelMapper(),
            schedulersFactory = TestSchedulersFactory,
        )

        viewModel.deleteById(1)
        assertTrue(viewModel.uiState.value.status is Status.Idle)
    }
}
