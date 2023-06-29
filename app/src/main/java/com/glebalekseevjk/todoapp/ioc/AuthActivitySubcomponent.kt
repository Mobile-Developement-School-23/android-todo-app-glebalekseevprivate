package com.glebalekseevjk.todoapp.ioc

import com.glebalekseevjk.todoapp.ioc.module.YandexAuthSdkModule
import com.glebalekseevjk.todoapp.ioc.scope.AuthActivitySubcomponentScope
import com.glebalekseevjk.todoapp.presentation.activity.AuthActivity
import dagger.Subcomponent

@AuthActivitySubcomponentScope
@Subcomponent(modules = [YandexAuthSdkModule::class])
interface AuthActivitySubcomponent {
    fun inject(activity: AuthActivity)
}