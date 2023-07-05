package com.glebalekseevjk.todoapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.data.repository.AuthRepository
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.injectMainActivity(this)
        super.onCreate(savedInstanceState)
        observeIsAuth()
        setContentView(R.layout.activity_main)
    }

    private fun observeIsAuth() {
        lifecycleScope.launch {
            authRepository.isAuth.collect {
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