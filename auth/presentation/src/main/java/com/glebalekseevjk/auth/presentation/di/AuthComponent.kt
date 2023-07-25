package com.glebalekseevjk.auth.presentation.di

import com.glebalekseevjk.auth.presentation.AuthActivity
import com.glebalekseevjk.auth.presentation.di.module.YandexAuthSdkModule
import com.glebalekseevjk.auth.presentation.di.scope.AuthComponentScope
import dagger.Component

@AuthComponentScope
@Component(
    dependencies = [AuthDependencies::class],
    modules = [YandexAuthSdkModule::class]
)
interface AuthComponent {
    fun inject(activity: AuthActivity)

    @Component.Factory
    interface Factory {
        fun create(dependencies: AuthDependencies): AuthComponent
    }
}