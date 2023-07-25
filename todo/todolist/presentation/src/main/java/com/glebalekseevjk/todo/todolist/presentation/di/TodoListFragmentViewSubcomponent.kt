package com.glebalekseevjk.todo.todolist.presentation.di

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.glebalekseevjk.todo.todolist.presentation.fragment.controller.TodoItemsViewController
import com.glebalekseevjk.todo.todolist.presentation.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.todo.todolist.presentation.di.scope.TodoListFragmentViewSubcomponentScope
import com.glebalekseevjk.todo.todolist.presentation.fragment.TodoItemsFragment
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.TodoItemsViewModel
import dagger.BindsInstance
import dagger.Subcomponent


@TodoListFragmentViewSubcomponentScope
@Subcomponent(modules = [])
interface TodoListFragmentViewSubcomponent {
    fun inject(fragment: TodoItemsFragment)

    val viewController: TodoItemsViewController

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun rootView(rootView: View): Builder

        @BindsInstance
        fun binding(binding: FragmentTodoItemsBinding): Builder

        @BindsInstance
        fun lifecycleOwner(lifecycleOwner: LifecycleOwner): Builder

        @BindsInstance
        fun todoItemsViewModel(todoItemsViewModel: TodoItemsViewModel): Builder

        @BindsInstance
        fun navController(navController: NavController): Builder

        @BindsInstance
        fun fragmentManager(fragmentManager: FragmentManager): Builder

        @BindsInstance
        fun todoItemsFragment(fragment: TodoItemsFragment): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): TodoListFragmentViewSubcomponent
    }
}