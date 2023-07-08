package com.glebalekseevjk.feature.todoitem.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.glebalekseevjk.feature.todoitem.databinding.FragmentTodoItemBinding
import com.glebalekseevjk.feature.todoitem.di.TodoItemFragmentSubcomponent
import com.glebalekseevjk.feature.todoitem.di.TodoItemFragmentViewSubcomponent
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class TodoItemFragment : Fragment() {
    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoItemViewModel: TodoItemViewModel
    private var fragmentComponent: TodoItemFragmentSubcomponent? = null
    private var fragmentViewComponent: TodoItemFragmentViewSubcomponent? = null
    private val args: TodoItemFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent =
            TodoItemsViewModel.fragmentComponent!!.createTodoItemFragmentSubcomponent()
                .apply {
                    inject(this@TodoItemFragment)
                }
        todoItemViewModel = ViewModelProvider(this, viewModelFactory)[TodoItemViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            parseParams()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        fragmentViewComponent = fragmentComponent!!
            .todoItemFragmentViewSubcomponentBuilder()
            .activity(requireActivity())
            .binding(binding)
            .navController(findNavController())
            .todoItemViewModel(todoItemViewModel)
            .lifecycleOwner(viewLifecycleOwner)
            .build()
        super.onViewCreated(view, savedInstanceState)
        fragmentViewComponent!!.viewController.setupViews()
        startPostponedEnterTransition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }

    private fun parseParams() {
        lifecycleScope.launch {
            todoItemViewModel.dispatch(TodoItemAction.Init(args.todoId))
        }
    }
}