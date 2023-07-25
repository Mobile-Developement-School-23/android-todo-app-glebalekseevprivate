package com.glebalekseevjk.todoapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.glebalekseevjk.auth.domain.entity.NightMode
import com.glebalekseevjk.auth.domain.usecase.AuthUseCase
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.auth.presentation.AuthActivity
import com.glebalekseevjk.todoapp.broadcastreceiver.NotificationReceiver.Companion.TODOITEM_ID
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Наблюдает за состоянием аутентификации и запускает активность аутентификации при необходимости.
 */
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authUseCase: AuthUseCase

    @Inject
    lateinit var personalUseCase: PersonalUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        setupTheme()
        super.onCreate(savedInstanceState)
        observeIsAuth()
        observeNightMode()
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    private fun setupTheme() {
        val theme = when (personalUseCase.nightMode) {
            NightMode.NIGHT -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Night
            NightMode.DAY -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Day
            NightMode.SYSTEM -> null
        }
        theme?.let {
            setTheme(it)
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }

    private fun observeIsAuth() {
        lifecycleScope.launch {
            authUseCase.isAuthAsFlow.collectLatest {
                if (!it) startAuthActivity() else {
                    setContentView(R.layout.activity_main)
                }
            }
        }
    }

    private fun observeNightMode() {
        lifecycleScope.launch {
            var counter = 1
            personalUseCase.nightModeAsFlow.collectLatest {
                if (counter == 0) recreate()
                else counter--
            }
        }
    }

    private fun startAuthActivity() {
        val intent = AuthActivity.createIntent(this)
        startActivity(intent)
        overridePendingTransition(
            com.glebalekseevjk.design.R.anim.slide_out_right,
            com.glebalekseevjk.design.R.anim.slide_in_right
        )
        finishAffinity()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}