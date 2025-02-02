package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun like(id : Long): Post
    suspend fun deleteLike(id: Long): Post
    suspend fun savePost(id: Long, content: String): Post
    suspend fun delete(id: Long)
}