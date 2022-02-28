package com.example.den.gitusercoroutines.repository

import android.util.Log
import android.widget.ImageView
import com.example.den.gitusercoroutines.api.GitApi
import com.example.den.gitusercoroutines.R
import com.example.den.gitusercoroutines.model.GitUser
import com.example.den.gitusercoroutines.model.GitUserDetail
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "GitRepo"

object GitUserRepo {
    private val gitApi: GitApi
    init {
        Log.i(TAG,"Create Repo")
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()
        gitApi = retrofit.create(GitApi::class.java);
    }

    suspend fun getUsers(): List<GitUser> {
        return gitApi.getUsers().await()
    }

    suspend fun getUserDetail(name : String): GitUserDetail {
        return gitApi.getUserDetail(name).await()
    }
    suspend fun getAvatar(url : String): ResponseBody {
        return gitApi.getAvatar(url).await()
    }
    fun getBitmapAvatar(url : String, imageView: ImageView){
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_404)
            .into(imageView)
    }
    suspend fun getPage(since : String): List<GitUser> {
        return gitApi.getNextPage(since).await()
    }
}