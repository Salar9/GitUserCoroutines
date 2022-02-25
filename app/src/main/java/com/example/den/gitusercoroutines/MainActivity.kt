package com.example.den.gitusercoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.den.gitusercoroutines.repository.GitUserDaoRepo
import com.example.den.gitusercoroutines.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        GitUserDaoRepo.initialize(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}