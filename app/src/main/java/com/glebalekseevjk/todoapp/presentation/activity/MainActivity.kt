package com.glebalekseevjk.todoapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.data.TodoItemsRepositoryImpl

val todoItemsRepositoryImpl = TodoItemsRepositoryImpl()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}