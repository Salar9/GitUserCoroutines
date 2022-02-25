package com.example.den.gitusercoroutines.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GitUserDB(
    @PrimaryKey val id: String,
    val login: String,
    val avatar_url: String,
)
