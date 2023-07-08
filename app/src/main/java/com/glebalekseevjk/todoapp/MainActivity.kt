package com.glebalekseevjk.todoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.feature.auth.presentation.AuthActivity
//import com.glebalekseevjk.domain.auth.AuthRepository
//import com.glebalekseevjk.feature.auth.presentation.AuthActivity
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        observeIsAuth()
        setContentView(R.layout.activity_main)
    }

    private fun observeIsAuth() {
        lifecycleScope.launch {
            authRepository.isAuth.collectLatest {
                if (!it) {
                    val intent = AuthActivity.createIntent(this@MainActivity)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}