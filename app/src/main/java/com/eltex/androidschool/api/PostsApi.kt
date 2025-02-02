package com.eltex.androidschool.api

import com.eltex.androidschool.model.Post
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostsApi {
    @GET("api/posts")
    suspend fun getPosts(): List<Post>

    @POST("api/posts/{id}/likes")
    suspend fun like(@Path("id") id: Long): Post

    @DELETE("api/posts/{id}/likes")
    suspend fun deleteLike(@Path("id") id: Long): Post

    @POST("api/posts")
    suspend fun save(@Body post: Post): Post

    @DELETE("api/posts/{id}")
    suspend fun delete(@Path("id") id: Long)

    companion object {
        val INSTANCE: PostsApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }
}