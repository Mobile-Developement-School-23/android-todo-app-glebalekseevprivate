package com.glebalekseevjk.todo.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.glebalekseevjk.core.utils.FragmentWithBinding
import com.glebalekseevjk.core.utils.di.DepsMap
import com.glebalekseevjk.core.utils.di.HasDependencies
import com.glebalekseevjk.core.utils.di.findDependencies
import com.glebalekseevjk.todo.presentation.databinding.FragmentTodoBinding
import com.glebalekseevjk.todo.presentation.di.DaggerTodoComponent
import javax.inject.Inject
import kotlin.reflect.KProperty

class TodoFragment : FragmentWithBinding<FragmentTodoBinding>(FragmentTodoBinding::inflate), HasDependencies {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerTodoComponent.factory()
            .create(findDependencies())
            .apply {
                inject(this@TodoFragment)
            }
    }

    @Inject
    override lateinit var depsMap: DepsMap
}