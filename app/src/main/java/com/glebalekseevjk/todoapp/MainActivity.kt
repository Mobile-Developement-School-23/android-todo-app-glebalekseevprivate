package com.glebalekseevjk.todoapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.glebalekseevjk.core.preferences.PersonalStorage
import com.glebalekseevjk.core.preferences.PersonalStorage.Companion.NightMode.DAY
import com.glebalekseevjk.core.preferences.PersonalStorage.Companion.NightMode.NIGHT
import com.glebalekseevjk.core.preferences.PersonalStorage.Companion.NightMode.SYSTEM
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.feature.auth.presentation.AuthActivity
import com.glebalekseevjk.feature.todoitem.presentation.fragment.TodoItemsFragmentDirections
import com.glebalekseevjk.todoapp.broadcastreceiver.NotificationReceiver.Companion.TODOITEM_ID
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Наблюдает за состоянием аутентификации и запускает активность аутентификации при необходимости.
 */
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var personalStorage: PersonalStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        runBlocking {
            val theme = when (personalStorage.nightMode.first()) {
                NIGHT -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Night
                DAY -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Day
                SYSTEM -> null
            }
            theme?.let {
                setTheme(it)
            }
        }
        super.onCreate(savedInstanceState)
        observeIsAuth()
        observeNightMode()
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val id = intent?.getStringExtra(TODOITEM_ID)

        println("----------- fun onPostCreate(savedInstanceState: Bundle?) { id=$id")
        id?.let {
            val fragmentContainer = findViewById<FragmentContainerView>(R.id.fragment_container_view)
            val navController =fragmentContainer.findNavController()
//            val navController =
//                findNavController(com.glebalekseevjk.feature.todoitem.R.id.app_navigation)
            val action =
                TodoItemsFragmentDirections.actionTodoItemsFragmentToTodoItemFragment(it)
            navController.navigate(action)
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
            authRepository.isAuth.collectLatest {
                if (!it) startAuthActivity() else {
                    setContentView(R.layout.activity_main)
                }
            }
        }
    }

    private fun observeNightMode() {
        lifecycleScope.launch {
            var counter = 1
            personalStorage.nightMode.collectLatest {
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