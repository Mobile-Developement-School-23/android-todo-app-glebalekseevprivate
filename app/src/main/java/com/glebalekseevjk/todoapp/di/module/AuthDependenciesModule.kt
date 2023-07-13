package com.glebalekseevjk.todoapp.di.module

import android.content.Context
import android.content.Intent
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.todoapp.MainActivity
import com.glebalekseevjk.todoapp.di.AppComponent
import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.core.utils.di.DependenciesKey
import com.glebalekseevjk.feature.auth.di.AuthDependencies
import com.glebalekseevjk.feature.auth.di.PlayIntent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface AuthDependenciesModule {
    @Binds
    @IntoMap
    @DependenciesKey(AuthDependencies::class)
    fun bindAuthDependencies(impl: AppComponent): Dependencies

    companion object {
        @PlayIntent
        @Provides
        fun providePlayIntent(@ApplicationContext context: Context): Intent {
            return MainActivity.createIntent(context)
        }
    }
}