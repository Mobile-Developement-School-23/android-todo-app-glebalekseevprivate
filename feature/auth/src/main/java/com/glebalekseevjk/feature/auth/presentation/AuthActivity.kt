package com.glebalekseevjk.feature.auth.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.core.preferences.PersonalStorage
import com.glebalekseevjk.core.preferences.PersonalStorage.Companion.NightMode.*
import com.glebalekseevjk.core.utils.di.findDependencies
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.feature.auth.R
import com.glebalekseevjk.feature.auth.databinding.ActivityAuthBinding
import com.glebalekseevjk.feature.auth.di.DaggerAuthComponent
import com.glebalekseevjk.feature.auth.di.PlayIntent
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.internal.AuthSdkActivity
import dagger.Lazy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject


class AuthActivity : AppCompatActivity() {
    private var _binding: ActivityAuthBinding? = null
    private val binding: ActivityAuthBinding
        get() = _binding ?: throw RuntimeException("ActivityAuthBinding is null")

    @Inject
    lateinit var yandexAuthSdk: Lazy<YandexAuthSdk>

    @Inject
    lateinit var yandexAuthLoginOptions: Lazy<YandexAuthLoginOptions>

    @Inject
    lateinit var authRepository: Lazy<AuthRepository>

    @Inject
    lateinit var personalStorage: PersonalStorage


    @Inject
    @PlayIntent
    lateinit var playIntent: Lazy<Intent>

    private val yandexAuthMutex = Mutex()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAuthComponent.factory()
            .create(findDependencies())
            .inject(this)
        super.onCreate(savedInstanceState)
        runBlocking {
            val theme = when(personalStorage.nightMode.first()){
                NIGHT -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Night
                DAY -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Day
                SYSTEM -> null
            }
            theme?.let {
                setTheme(it)
            }
        }

        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.yandexAuthBtn.setOnClickListener {
            startYandexAuth()
        }
        binding.authBtn.setOnClickListener {
            // TODO
        }
        binding.guestAuthBtn.setOnClickListener {
            // TODO
        }
    }

    private fun startYandexAuth() {
        lifecycleScope.launch {
            if (yandexAuthMutex.isLocked) return@launch
            yandexAuthMutex.withLock {
                val intent = yandexAuthSdk.get().createLoginIntent(yandexAuthLoginOptions.get())
                ActivityCompat.startActivityForResult(
                    this@AuthActivity,
                    intent,
                    AuthSdkActivity.LOGIN_REQUEST_CODE,
                    null
                )
                delay(1000)
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AuthSdkActivity.LOGIN_REQUEST_CODE) {
            try {
                checkToken(resultCode, data)
            } catch (_: YandexAuthException) {

            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startMainActivity() {
        val intent = playIntent.get()
        startActivity(intent)
        finishAffinity()
    }

    private fun checkToken(resultCode: Int, data: Intent?) {
        val yandexAuthToken = yandexAuthSdk.get().extractToken(resultCode, data)
        if (yandexAuthToken != null) {
            lifecycleScope.launch {
                authRepository.get().authorize(yandexAuthToken.value)
                startMainActivity()
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}