package com.glebalekseevjk.todoapp.presentation.fragment

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
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemBinding
import com.glebalekseevjk.todoapp.ioc.TodoItemFragmentSubcomponent
import com.glebalekseevjk.todoapp.ioc.TodoItemFragmentViewSubcomponent
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.launch
import javax.inject.Inject


class TodoItemFragment : Fragment() {
    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoViewModel: TodoItemViewModel
    private var fragmentComponent: TodoItemFragmentSubcomponent? = null
    private var fragmentViewComponent: TodoItemFragmentViewSubcomponent? = null
    private val args: TodoItemFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent = context.appComponent.createTodoItemFragmentSubcomponent()
            .apply {
                inject(this@TodoItemFragment)
            }
        todoViewModel = ViewModelProvider(this, viewModelFactory)[TodoItemViewModel::class.java]
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
            .todoItemViewModel(todoViewModel)
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
            todoViewModel.dispatch(TodoItemAction.Init(args.todoId))
        }
    }
}