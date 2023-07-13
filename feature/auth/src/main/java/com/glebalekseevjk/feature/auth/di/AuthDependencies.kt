package com.glebalekseevjk.feature.auth.di

import android.content.Context
import android.content.Intent
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.domain.auth.AuthRepository
import javax.inject.Qualifier

/**
 * Этот интерфейс определяет список необходимых модулю :feature:auth зависимостей.
 *
 */
interface AuthDependencies : Dependencies {
    val authRepository: AuthRepository

    @PlayIntent
    fun playIntent(): Intent

    @ApplicationContext
    fun getContext(): Context
}

@Qualifier
annotation class PlayIntent