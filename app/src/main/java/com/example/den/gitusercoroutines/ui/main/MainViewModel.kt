package com.example.den.gitusercoroutines.ui.main

import androidx.lifecycle.ViewModel
import com.example.den.gitusercoroutines.repository.GitUserRepo

class MainViewModel : ViewModel() {
    val gitRepo = GitUserRepo()
}