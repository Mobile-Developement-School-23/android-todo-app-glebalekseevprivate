package com.glebalekseevjk.feature.auth.di.module

import android.content.Context
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
            context: Context,
            yandexAuthOptions: YandexAuthOptions
        ): YandexAuthSdk {
            return YandexAuthSdk(context, yandexAuthOptions)
        }

        @Provides
        fun provideYandexAuthOptions(
            context: Context
        ): YandexAuthOptions {
            return YandexAuthOptions(context, loggingEnabled = true)
        }

        @Provides
        fun provideYandexAuthLoginOptions(): YandexAuthLoginOptions {
            return YandexAuthLoginOptions.Builder().build()
        }
    }
}