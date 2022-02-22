package com.example.den.gitusercoroutines.api

import com.example.den.gitusercoroutines.model.*
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.*

interface GitApi {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    fun getUsers(): Deferred<List<GitUser>>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users/{username}")
    fun getUserDetail(@Path("username") username: String): Deferred<GitUserDetail>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    fun getNextPage(@Query("since") query : String): Deferred<List<GitUser>>

    @GET
    fun getAvatar(@Url url: String): Deferred<ResponseBody>
}