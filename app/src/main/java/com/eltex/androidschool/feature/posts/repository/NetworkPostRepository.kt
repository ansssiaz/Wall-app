package com.eltex.androidschool.feature.posts.repository

import com.eltex.androidschool.feature.posts.api.PostsApi
import com.eltex.androidschool.feature.posts.data.Post

class NetworkPostRepository(private val api: PostsApi) :
    PostRepository {
    override suspend fun savePost(id: Long, content: String): Post {
        return api.save(
            Post(
                id = id,
                content = content,
            )
        )
    }

    override suspend fun delete(id: Long) = api.delete(id)

    override suspend fun getLatest(count: Int): List<Post> = api.getLatest(count)

    override suspend fun getBefore(id: Long, count: Int): List<Post> = api.getBefore(id, count)

    override suspend fun like(id: Long) = api.like(id)

    override suspend fun deleteLike(id: Long) = api.deleteLike(id)
}