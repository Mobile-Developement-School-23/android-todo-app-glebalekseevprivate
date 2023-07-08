package com.glebalekseevjk.feature.todoitem.di

import com.glebalekseevjk.feature.todoitem.di.scope.TodoItemFragmentSubcomponentScope
import com.glebalekseevjk.feature.todoitem.presentation.fragment.TodoItemFragment
import dagger.Subcomponent

@TodoItemFragmentSubcomponentScope
@Subcomponent(modules = [])
interface TodoItemFragmentSubcomponent {
    fun inject(fragment: TodoItemFragment)
    fun todoItemFragmentViewSubcomponentBuilder(): TodoItemFragmentViewSubcomponent.Builder
}
