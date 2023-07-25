package com.glebalekseevjk.todo.presentation.di.module

import androidx.navigation.NavController
import com.glebalekseevjk.todo.todolist.presentation.di.NavigateToTodoEdit
import com.glebalekseevjk.todo.todolist.presentation.fragment.TodoItemsFragmentDirections
import dagger.Module
import dagger.Provides


@Module
interface NavigationModule {
    companion object {
        @NavigateToTodoEdit
        @Provides
        fun navigateToTodoEdit(): (String, NavController) -> Unit = { todoId, navController ->
            val action =
                TodoItemsFragmentDirections.actionTodoItemsFragmentToTodoItemFragment(todoId)
            navController.navigate(action)
        }
    }
}

