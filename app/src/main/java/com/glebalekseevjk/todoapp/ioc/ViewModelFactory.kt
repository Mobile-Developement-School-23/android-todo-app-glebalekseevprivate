package com.glebalekseevjk.todoapp.ioc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import javax.inject.Inject
import javax.inject.Provider

@AppComponentScope
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}