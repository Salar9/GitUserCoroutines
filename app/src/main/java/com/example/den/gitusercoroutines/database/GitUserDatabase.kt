package com.example.den.gitusercoroutines.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.den.gitusercoroutines.model.GitUserDB

@Database(entities = [ GitUserDB::class ], version=1)

abstract class GitUserDatabase  : RoomDatabase() {
    abstract fun gitUserDao(): GitUserDao
}