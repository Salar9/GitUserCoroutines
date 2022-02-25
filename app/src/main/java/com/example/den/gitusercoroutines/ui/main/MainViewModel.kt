package com.example.den.gitusercoroutines.ui.main

import androidx.lifecycle.ViewModel
import com.example.den.gitusercoroutines.model.GitUser
import com.example.den.gitusercoroutines.repository.GitUserDaoRepo
import com.example.den.gitusercoroutines.repository.GitUserRepo

class MainViewModel : ViewModel() {
    var gitUsers = listOf<GitUser>()

    val gitUserRepo = GitUserRepo()
    val gitUserDaoRepo = GitUserDaoRepo.get()
}