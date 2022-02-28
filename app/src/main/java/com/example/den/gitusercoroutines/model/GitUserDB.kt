package com.example.den.gitusercoroutines.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GitUserDB(
    @PrimaryKey val id: String,
    val login: String,
    val avatar_url: String,
)
