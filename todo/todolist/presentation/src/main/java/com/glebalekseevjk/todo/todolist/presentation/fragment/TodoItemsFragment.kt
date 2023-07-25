package com.glebalekseevjk.todo.todolist.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.glebalekseevjk.core.utils.FragmentWithBinding
import com.glebalekseevjk.core.utils.di.findDependencies
import com.glebalekseevjk.todo.todolist.presentation.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.todo.todolist.presentation.di.DaggerTodoListComponent
import com.glebalekseevjk.todo.todolist.presentation.di.TodoListComponent
import com.glebalekseevjk.todo.todolist.presentation.di.TodoListFragmentViewSubcomponent
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.TodoItemsViewModel
import javax.inject.Inject


/**
Этот класс отвечает за отображение списка задач
 */
class TodoItemsFragment :
    FragmentWithBinding<FragmentTodoItemsBinding>(FragmentTodoItemsBinding::inflate) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoItemsViewModel: TodoItemsViewModel
    private var fragmentComponent: TodoListComponent? = null
    private var fragmentViewComponent: TodoListFragmentViewSubcomponent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent = DaggerTodoListComponent.factory()
            .create(findDependencies())
            .apply {
                inject(this@TodoItemsFragment)
            }
        todoItemsViewModel =
            ViewModelProvider(this, viewModelFactory)[TodoItemsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        fragmentViewComponent = fragmentComponent!!
            .todoListFragmentViewSubcomponentBuilder()
            .rootView(requireView())
            .binding(binding)
            .navController(findNavController())
            .todoItemsViewModel(todoItemsViewModel)
            .fragmentManager(parentFragmentManager)
            .lifecycleOwner(viewLifecycleOwner)
            .todoItemsFragment(this)
            .context(requireContext())
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