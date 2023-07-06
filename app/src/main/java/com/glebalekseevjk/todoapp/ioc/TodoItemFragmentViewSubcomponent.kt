package com.glebalekseevjk.todoapp.ioc

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemBinding
import com.glebalekseevjk.todoapp.ioc.scope.TodoItemFragmentViewSubcomponentScope
import com.glebalekseevjk.todoapp.presentation.fragment.TodoItemFragment
import com.glebalekseevjk.todoapp.presentation.fragment.controller.TodoItemViewController
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemViewModel
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