package com.glebalekseevjk.auth.presentation.di.module

import android.content.Context
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides

@Module
interface YandexAuthSdkModule {
    companion object {
        @Provides
        fun provideYandexAuthSdk(
            @ApplicationContext context: Context,
            yandexAuthOptions: YandexAuthOptions
        ): YandexAuthSdk {
            return YandexAuthSdk(context, yandexAuthOptions)
        }

        @Provides
        fun provideYandexAuthOptions(
            @ApplicationContext context: Context
        ): YandexAuthOptions {
            return YandexAuthOptions(context, loggingEnabled = true)
        }

        @Provides
        fun provideYandexAuthLoginOptions(): YandexAuthLoginOptions {
            return YandexAuthLoginOptions.Builder().build()
        }
    }
}