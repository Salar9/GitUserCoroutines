package com.example.den.gitusercoroutines.api

import com.example.den.gitusercoroutines.model.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface GitApi {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    suspend fun getUsers(): List<GitUser>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): GitUserDetail

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    suspend fun getNextPage(@Query("since") query : String): List<GitUser>

    @GET
    suspend fun getAvatar(@Url url: String): ResponseBody
}