package com.glebalekseevjk.auth.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.auth.domain.di.PlayIntent
import com.glebalekseevjk.auth.domain.entity.NightMode
import com.glebalekseevjk.auth.domain.usecase.AuthUseCase
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.auth.presentation.databinding.ActivityAuthBinding
import com.glebalekseevjk.auth.presentation.di.DaggerAuthComponent
import com.glebalekseevjk.core.utils.AppCompatActivityWithBinding
import com.glebalekseevjk.core.utils.di.findDependencies
import com.yandex.authsdk.BuildConfig
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.internal.AuthSdkActivity
import dagger.Lazy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject


class AuthActivity :
    AppCompatActivityWithBinding<ActivityAuthBinding>(ActivityAuthBinding::inflate) {
    @Inject
    lateinit var yandexAuthSdk: Lazy<YandexAuthSdk>

    @Inject
    lateinit var yandexAuthLoginOptions: Lazy<YandexAuthLoginOptions>

    @Inject
    lateinit var authUseCase: Lazy<AuthUseCase>

    @Inject
    lateinit var personalUseCase: Lazy<PersonalUseCase>

    @Inject
    @PlayIntent
    lateinit var playIntent: Lazy<Intent>

    private val yandexAuthMutex = Mutex()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAuthComponent.factory()
            .create(findDependencies())
            .inject(this)
        super.onCreate(savedInstanceState)
        setupTheme()
        setContentView(binding.root)
        initListeners()
    }

    private fun setupTheme() {
        val theme = when (personalUseCase.get().nightMode) {
            NightMode.NIGHT -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Night
            NightMode.DAY -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Day
            NightMode.SYSTEM -> null
        }
        theme?.let {
            setTheme(it)
        }
    }

    private fun initListeners() {
        binding.yandexAuthBtn.setOnClickListener {
            startYandexAuth()
        }
        binding.authBtn.setOnClickListener {
            lifecycleScope.launch {
                authUseCase.get()
                    .bearerAuthorization(com.glebalekseevjk.auth.presentation.BuildConfig.YANDEX_BEARER_TOKEN)
                startMainActivity()
            }
        }
        binding.guestAuthBtn.setOnClickListener {
            // TODO: guest mode
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

    private fun checkToken(resultCode: Int, data: Intent?) {
        val yandexAuthToken = yandexAuthSdk.get().extractToken(resultCode, data)
        if (yandexAuthToken != null) {
            lifecycleScope.launch {
                authUseCase.get().oauthAuthorization(yandexAuthToken.value)
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        val intent = playIntent.get()
        startActivity(intent)
        finishAffinity()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}