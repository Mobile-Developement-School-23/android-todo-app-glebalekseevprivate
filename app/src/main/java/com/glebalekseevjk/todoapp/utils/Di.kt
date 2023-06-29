package com.glebalekseevjk.todoapp.utils

import android.content.Context
import com.glebalekseevjk.todoapp.App
import com.glebalekseevjk.todoapp.ioc.AppComponent

val Context.appComponent: AppComponent
    get() = when(this){
        is App -> {
            appComponent
        }
        else -> {
            this.applicationContext.appComponent
        }
    }