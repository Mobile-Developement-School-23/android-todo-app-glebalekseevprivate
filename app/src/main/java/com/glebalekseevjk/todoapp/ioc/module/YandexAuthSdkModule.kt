package com.glebalekseevjk.todoapp.ioc.module

import android.content.Context
import com.glebalekseevjk.todoapp.ioc.scope.AuthActivitySubcomponentScope
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides

@Module
interface YandexAuthSdkModule {
    companion object {
        @AuthActivitySubcomponentScope
        @Provides
        fun provideYandexAuthSdk(
            context: Context,
            yandexAuthOptions: YandexAuthOptions
        ): YandexAuthSdk {
            return YandexAuthSdk(context, yandexAuthOptions)
        }

        @AuthActivitySubcomponentScope
        @Provides
        fun provideYandexAuthOptions(
            context: Context
        ): YandexAuthOptions {
            return YandexAuthOptions(context, loggingEnabled = true)
        }

        @AuthActivitySubcomponentScope
        @Provides
        fun provideYandexAuthLoginOptions(): YandexAuthLoginOptions {
            return YandexAuthLoginOptions.Builder().build()
        }
    }
}