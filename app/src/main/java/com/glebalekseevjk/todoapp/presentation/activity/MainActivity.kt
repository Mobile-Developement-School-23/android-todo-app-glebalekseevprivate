package com.glebalekseevjk.todoapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.data.TodoItemsRepositoryImpl

val todoItemsRepositoryImpl = TodoItemsRepositoryImpl()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}