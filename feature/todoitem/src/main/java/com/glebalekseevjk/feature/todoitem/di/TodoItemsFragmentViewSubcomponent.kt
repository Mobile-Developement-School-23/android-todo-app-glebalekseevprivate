package com.glebalekseevjk.feature.todoitem.di

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.glebalekseevjk.feature.todoitem.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.feature.todoitem.di.scope.TodoItemsFragmentViewSubcomponentScope
import com.glebalekseevjk.feature.todoitem.presentation.fragment.TodoItemsFragment
import com.glebalekseevjk.feature.todoitem.presentation.fragment.controller.TodoItemsViewController
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemsViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@TodoItemsFragmentViewSubcomponentScope
@Subcomponent(modules = [])
interface TodoItemsFragmentViewSubcomponent {
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

        fun build(): TodoItemsFragmentViewSubcomponent
    }
}