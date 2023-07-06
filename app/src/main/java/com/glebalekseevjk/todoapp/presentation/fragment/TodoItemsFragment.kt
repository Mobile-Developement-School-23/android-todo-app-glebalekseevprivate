package com.glebalekseevjk.todoapp.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.todoapp.ioc.TodoItemsFragmentSubcomponent
import com.glebalekseevjk.todoapp.ioc.TodoItemsFragmentViewSubcomponent
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsViewModel
import com.glebalekseevjk.todoapp.utils.appComponent
import javax.inject.Inject


class TodoItemsFragment : Fragment() {
    private var _binding: FragmentTodoItemsBinding? = null
    private val binding: FragmentTodoItemsBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoItemsViewModel: TodoItemsViewModel
    private var fragmentComponent: TodoItemsFragmentSubcomponent? = null
    private var fragmentViewComponent: TodoItemsFragmentViewSubcomponent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent = context.appComponent.createTodoItemsFragmentSubcomponent()
            .apply {
                inject(this@TodoItemsFragment)
            }
        todoItemsViewModel =
            ViewModelProvider(this, viewModelFactory)[TodoItemsViewModel::class.java]
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
        fragmentViewComponent = fragmentComponent!!
            .todoItemFragmentSubcomponentBuilder()
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