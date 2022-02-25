package com.example.den.gitusercoroutines.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GitUserDao {
    @Query("SELECT * FROM GitUserDB")
    suspend fun getGitUsers(): List<GitUserDB>

    @Insert
    suspend fun addGitUser(gitUser: List<GitUserDB>)
}