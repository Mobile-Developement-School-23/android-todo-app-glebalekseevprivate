package com.glebalekseevjk.feature.todoitem.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.glebalekseevjk.core.utils.di.findDependencies
import com.glebalekseevjk.feature.todoitem.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.feature.todoitem.di.DaggerTodoItemsComponent
import com.glebalekseevjk.feature.todoitem.di.TodoItemsComponent
import com.glebalekseevjk.feature.todoitem.di.TodoItemsFragmentViewSubcomponent
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemsViewModel
import javax.inject.Inject


class TodoItemsFragment : Fragment() {
    private var _binding: FragmentTodoItemsBinding? = null
    private val binding: FragmentTodoItemsBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoItemsViewModel: TodoItemsViewModel
    private var fragmentViewComponent: TodoItemsFragmentViewSubcomponent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerTodoItemsComponent.factory()
            .create(findDependencies())
            .apply {
                inject(this@TodoItemsFragment)
            }
        todoItemsViewModel = ViewModelProvider(this, viewModelFactory)[TodoItemsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        fragmentViewComponent = todoItemsViewModel.fragmentComponent
            .todoItemsFragmentViewSubcomponentBuilder()
            .rootView(requireView())
            .binding(binding)
            .navController(findNavController())
            .todoItemsViewModel(todoItemsViewModel)
            .lifecycleOwner(viewLifecycleOwner)
            .build()
        super.onViewCreated(view, savedInstanceState)
        fragmentViewComponent!!.viewController.setupViews()
        startPostponedEnterTransition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
    }
}