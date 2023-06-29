package com.glebalekseevjk.todoapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.todoapp.data.repository.AuthRepository
import com.glebalekseevjk.todoapp.databinding.ActivityAuthBinding
import com.glebalekseevjk.todoapp.utils.appComponent
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.internal.AuthSdkActivity
import dagger.Lazy
import kotlinx.coroutines.launch
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

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.createAuthActivitySubcomponent().inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.yandexAuthBtn.setOnClickListener {
            val intent = yandexAuthSdk.get().createLoginIntent(yandexAuthLoginOptions.get())
            ActivityCompat.startActivityForResult(
                this,
                intent,
                AuthSdkActivity.LOGIN_REQUEST_CODE,
                null
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AuthSdkActivity.LOGIN_REQUEST_CODE) {
            try {
                val yandexAuthToken = yandexAuthSdk.get().extractToken(resultCode, data)
                if (yandexAuthToken != null) {
                    lifecycleScope.launch {
                        authRepository.get().authorize(yandexAuthToken.value)
                        val intent = MainActivity.createIntent(this@AuthActivity)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
            } catch (_: YandexAuthException) {

            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}