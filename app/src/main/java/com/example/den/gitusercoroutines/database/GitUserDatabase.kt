package com.example.den.gitusercoroutines.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ GitUserDB::class ], version=1)

abstract class GitUserDatabase  : RoomDatabase() {
    abstract fun gitUserDao(): GitUserDao
}