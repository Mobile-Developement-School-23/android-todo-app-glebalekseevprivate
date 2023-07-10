package com.glebalekseevjk.feature.auth.di

import com.glebalekseevjk.feature.auth.di.module.YandexAuthSdkModule
import com.glebalekseevjk.feature.auth.presentation.AuthActivity
import com.glebalekseevjk.feature.auth.di.scope.AuthComponentScope
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