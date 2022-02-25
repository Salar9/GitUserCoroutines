package com.example.den.gitusercoroutines.repository

import android.content.Context
import androidx.room.Room
import com.example.den.gitusercoroutines.database.GitUserDB
import com.example.den.gitusercoroutines.database.GitUserDatabase


private const val DATABASE_NAME = "git-user-database"

class GitUserDaoRepo private constructor(context: Context) {
    private val database : GitUserDatabase = Room.databaseBuilder(
        context.applicationContext,
        GitUserDatabase::class.java,
        DATABASE_NAME
    ).build()

    suspend fun addToFavorites(users: List<GitUserDB>) = database.gitUserDao().addGitUser(users)
    suspend fun getFavoritesUser() : List<GitUserDB> = database.gitUserDao().getGitUsers()

    companion object {
        private var INSTANCE: GitUserDaoRepo? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GitUserDaoRepo(context)
            }
        }
        fun get(): GitUserDaoRepo {
            return INSTANCE ?:
            throw IllegalStateException("GitUserDaoRepo must be initialized")
        }
    }
}