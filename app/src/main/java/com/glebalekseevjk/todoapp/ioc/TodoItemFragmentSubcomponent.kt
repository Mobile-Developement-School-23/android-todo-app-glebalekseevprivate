package com.glebalekseevjk.todoapp.ioc

import com.glebalekseevjk.todoapp.ioc.scope.TodoItemFragmentSubcomponentScope
import com.glebalekseevjk.todoapp.presentation.fragment.TodoItemFragment
import dagger.Subcomponent

@TodoItemFragmentSubcomponentScope
@Subcomponent(modules = [])
interface TodoItemFragmentSubcomponent {
    fun inject(fragment: TodoItemFragment)
    fun todoItemFragmentViewSubcomponentBuilder(): TodoItemFragmentViewSubcomponent.Builder
}