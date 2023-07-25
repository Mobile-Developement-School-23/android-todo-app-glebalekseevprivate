package com.glebalekseevjk.todo.todoedit.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glebalekseevjk.auth.domain.entity.NightMode
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.todo.todoedit.presentation.R
import com.glebalekseevjk.todo.todoedit.presentation.fragment.TodoItemFragment
import com.glebalekseevjk.todo.todoedit.presentation.fragment.TodoItemFragment.Companion.TODO_ID
import javax.inject.Inject

class TodoEditActivity : AppCompatActivity() {
    @Inject
    lateinit var personalUseCase: PersonalUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTheme()
        setContentView(R.layout.activity_todo_edit)
        val id = intent?.getStringExtra(TODO_ID)
        id?.let {
            startTodoEditFragmentWithId(it)
        }
    }

    private fun setupTheme() {
        val theme = when (personalUseCase.nightMode) {
            NightMode.NIGHT -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Night
            NightMode.DAY -> com.glebalekseevjk.design.R.style.Theme_ToDoApp_Day
            NightMode.SYSTEM -> null
        }
        theme?.let {
            setTheme(it)
        }
    }

    private fun startTodoEditFragmentWithId(id: String) {
        val newFragment = TodoItemFragment.newInstance(id)
        supportFragmentManager.beginTransaction()
            .apply { replace(R.id.fragment_container_view, newFragment) }
            .commit()
    }
}