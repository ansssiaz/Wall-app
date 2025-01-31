package com.eltex.androidschool.api

import com.eltex.androidschool.model.Post
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostsApi {
    @GET("api/posts")
    fun getPosts(): Single<List<Post>>

    @POST("api/posts/{id}/likes")
    fun like(@Path("id") id: Long): Single<Post>

    @DELETE("api/posts/{id}/likes")
    fun deleteLike(@Path("id") id: Long): Single<Post>

    @POST("api/posts")
    fun save(@Body post: Post): Single<Post>

    @DELETE("api/posts/{id}")
    fun delete(@Path("id") id: Long): Completable

    companion object {
        val INSTANCE: PostsApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }
}