package com.glebalekseevjk.todoapp.ioc

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.todoapp.ioc.scope.TodoItemsFragmentViewSubcomponentScope
import com.glebalekseevjk.todoapp.presentation.fragment.TodoItemsFragment
import com.glebalekseevjk.todoapp.presentation.fragment.controller.TodoItemsViewController
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsViewModel
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
        fun build(): TodoItemsFragmentViewSubcomponent
    }
}