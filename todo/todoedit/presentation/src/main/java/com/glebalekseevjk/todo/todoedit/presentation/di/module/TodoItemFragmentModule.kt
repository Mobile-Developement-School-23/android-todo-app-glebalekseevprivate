package com.glebalekseevjk.todo.todoedit.presentation.di.module

import android.content.Context
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.design.R
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Qualifier

@Module
interface TodoItemFragmentModule {
    companion object {
        @Provides
        @DateFormatter
        fun provideDateFormatter(@ApplicationContext context: Context): SimpleDateFormat {
            return SimpleDateFormat(
                context.resources.getString(R.string.date_pattern),
                Locale("ru")
            )
        }

        @Provides
        @TimeFormatter
        fun provideTimeFormatter(@ApplicationContext context: Context): SimpleDateFormat {
            return SimpleDateFormat(
                context.resources.getString(R.string.time_pattern),
                Locale("ru")
            )
        }
    }
}

@Qualifier
annotation class DateFormatter

@Qualifier
annotation class TimeFormatter