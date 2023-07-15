package com.glebalekseevjk.feature.todoitem.di.module

import android.content.Context
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.design.R
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.Locale

@Module
interface TodoItemFragmentModule {
    companion object {
        @Provides
        fun provideSimpleDateFormat(@ApplicationContext context: Context): SimpleDateFormat {
            return SimpleDateFormat(
                context.resources.getString(R.string.date_pattern),
                Locale("ru")
            )
        }
    }
}