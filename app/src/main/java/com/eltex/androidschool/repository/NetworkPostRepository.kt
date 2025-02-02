package com.eltex.androidschool.repository

import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Post

class NetworkPostRepository(private val api: PostsApi) : PostRepository {

    override suspend fun getPosts(): List<Post> = api.getPosts()

    override suspend fun savePost(id: Long, content: String): Post {
        return api.save(
            Post(
                id = id,
                content = content,
            )
        )
    }

    override suspend fun delete(id: Long) = api.delete(id)

    override suspend fun like(id: Long): Post = api.like(id)

    override suspend fun deleteLike(id: Long): Post = api.deleteLike(id)
}