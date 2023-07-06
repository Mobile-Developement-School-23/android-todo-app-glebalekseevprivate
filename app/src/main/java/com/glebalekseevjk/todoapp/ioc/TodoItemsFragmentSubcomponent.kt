package com.glebalekseevjk.todoapp.ioc

import com.glebalekseevjk.todoapp.ioc.scope.TodoItemsFragmentSubcomponentScope
import com.glebalekseevjk.todoapp.presentation.fragment.TodoItemsFragment
import dagger.Subcomponent

@TodoItemsFragmentSubcomponentScope
@Subcomponent(modules = [])
interface TodoItemsFragmentSubcomponent {
    fun inject(fragment: TodoItemsFragment)
    fun todoItemFragmentSubcomponentBuilder(): TodoItemsFragmentViewSubcomponent.Builder
}