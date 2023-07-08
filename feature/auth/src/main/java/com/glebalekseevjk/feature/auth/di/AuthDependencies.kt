package com.glebalekseevjk.feature.auth.di

import android.content.Context
import android.content.Intent
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.core.utils.di.Dependencies
import javax.inject.Qualifier

interface AuthDependencies: Dependencies {
    val authRepository: AuthRepository
    val playIntent: PlayIntent
    val context: Context
}

data class PlayIntent(val intent: Intent)
