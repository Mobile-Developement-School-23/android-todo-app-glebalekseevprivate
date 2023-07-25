package com.glebalekseevjk.auth.presentation.di

import android.content.Context
import android.content.Intent
import com.glebalekseevjk.auth.domain.di.PlayIntent
import com.glebalekseevjk.auth.domain.repository.AuthRepository
import com.glebalekseevjk.auth.domain.repository.PersonalRepository
import com.glebalekseevjk.auth.domain.usecase.AuthUseCase
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.di.Dependencies


interface AuthDependencies : Dependencies {
    val authUseCase: AuthUseCase
    val personalUseCase: PersonalUseCase

    @PlayIntent
    fun playIntent(): Intent

    @ApplicationContext
    fun getContext(): Context
}
