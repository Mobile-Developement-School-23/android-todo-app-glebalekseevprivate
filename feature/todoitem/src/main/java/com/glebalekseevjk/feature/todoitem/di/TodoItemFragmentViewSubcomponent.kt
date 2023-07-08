package com.glebalekseevjk.feature.todoitem.di

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.glebalekseevjk.design.R
import com.glebalekseevjk.feature.todoitem.databinding.FragmentTodoItemBinding
import com.glebalekseevjk.feature.todoitem.di.scope.TodoItemFragmentViewSubcomponentScope
import com.glebalekseevjk.feature.todoitem.presentation.fragment.TodoItemFragment
import com.glebalekseevjk.feature.todoitem.presentation.fragment.controller.TodoItemViewController
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.text.SimpleDateFormat
import java.util.Locale

@TodoItemFragmentViewSubcomponentScope
@Subcomponent(modules = [TodoItemFragmentModule::class])
interface TodoItemFragmentViewSubcomponent {
    fun inject(fragment: TodoItemFragment)
    val viewController: TodoItemViewController

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: Activity): Builder

        @BindsInstance
        fun binding(binding: FragmentTodoItemBinding): Builder

        @BindsInstance
        fun lifecycleOwner(lifecycleOwner: LifecycleOwner): Builder

        @BindsInstance
        fun todoItemViewModel(todoItemViewModel: TodoItemViewModel): Builder

        @BindsInstance
        fun navController(navController: NavController): Builder
        fun build(): TodoItemFragmentViewSubcomponent
    }
}

@Module
interface TodoItemFragmentModule {
    companion object {
        @Provides
        fun provideSimpleDateFormat(context: Context): SimpleDateFormat {
            return SimpleDateFormat(
                context.resources.getString(R.string.date_pattern),
                Locale("ru")
            )
        }
    }
}