package com.glebalekseevjk.todoapp.presentation.fragment

import android.animation.LayoutTransition
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.todoapp.presentation.rv.adapter.TodoItemsAdapter
import com.glebalekseevjk.todoapp.presentation.rv.callback.SwipeCallback
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsAction
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsState
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsViewModel
import com.glebalekseevjk.todoapp.utils.getColorFromTheme
import kotlinx.coroutines.launch
import kotlin.math.pow


class TodoItemsFragment : Fragment() {
    private var _binding: FragmentTodoItemsBinding? = null
    private val binding: FragmentTodoItemsBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemsBinding is null")
    private lateinit var todoItemsViewModel: TodoItemsViewModel
    private lateinit var todoItemsAdapter: TodoItemsAdapter
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoItemsViewModel = ViewModelProvider(this)[TodoItemsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigationUI()
        initListeners()
        initUI()
        initAppBar()
        initTodoItemsState()
        setupRecyclerView()
        observeTodoItemsState()
    }

    private fun initNavigationUI() {
        navController = findNavController()
    }

    private fun initListeners() {
        binding.addTodoBtn.setOnClickListener {
            navigateToTodoItemFragmentWithAddMode()
        }
        binding.isShowDone.setOnClickListener {
            todoItemsViewModel.dispatch(TodoItemsAction.ChangeVisibility)
        }
    }

    private fun initUI() {
        binding.linearLayout.layoutTransition = LayoutTransition().apply {
            enableTransitionType(LayoutTransition.CHANGING)
        }
        binding.materialCardView.layoutTransition = LayoutTransition().apply {
            enableTransitionType(LayoutTransition.CHANGING)
        }
    }

    private fun initAppBar() {
        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val relativePosition =
                (appBarLayout.totalScrollRange + verticalOffset).toFloat() / appBarLayout.totalScrollRange
            val alpha = relativePosition.toDouble().pow(3.0).toFloat()
            binding.countDoneTodoTv.alpha = if (alpha < 0.1) 0f else alpha
        }
    }

    private fun initTodoItemsState() {
        lifecycleScope.launch {
            todoItemsViewModel.dispatch(TodoItemsAction.Init)
        }
    }

    private fun setupRecyclerView() {
        todoItemsAdapter = TodoItemsAdapter()
        val swipeCallback = SwipeCallback(resources.getDimension(R.dimen.swipe_backlash))
        with(binding.todoListRv) {
            adapter = todoItemsAdapter
            ItemTouchHelper(swipeCallback).attachToRecyclerView(this)
        }
        todoItemsAdapter.editClickListener = { todoId ->
            navigateToTodoItemFragmentWithEditMode(todoId)
        }
        todoItemsAdapter.changeDoneStatusClickListener = { todoId ->
            todoItemsViewModel.dispatch(TodoItemsAction.ChangeDoneStatus(todoId, requireContext()))
        }
        todoItemsAdapter.setDoneStatusClickListener = { todoId, viewHolder ->
            swipeCallback.resetViewHolder(viewHolder)
            todoItemsViewModel.dispatch(TodoItemsAction.SetDoneStatus(todoId, requireContext()))


        }
        todoItemsAdapter.addTodoItemClickListener = {
            navigateToTodoItemFragmentWithAddMode()
        }
        todoItemsAdapter.deleteTodoItemClickListener = { todoId, viewHolder ->
            swipeCallback.resetViewHolder(viewHolder)
            todoItemsViewModel.dispatch(TodoItemsAction.DeleteTodoItem(todoId, requireContext()))
        }
    }

    private fun observeTodoItemsState() {
        lifecycleScope.launch {
            todoItemsViewModel.todoItemsState.collect { todoItemsState ->
                when (todoItemsState) {
                    is TodoItemsState.Loaded -> {
                        todoItemsAdapter.submitList(todoItemsState.todoItemsDisplay)
                        binding.countDoneTodoTv.text = String.format(
                            resources.getString(R.string.count_done),
                            todoItemsState.todoItems.filter { it.isDone }.size
                        )
                        checkVisibilityButton(todoItemsState.visibility)
                    }

                    is TodoItemsState.Init -> {
                        binding.countDoneTodoTv.text =
                            String.format(resources.getString(R.string.count_done), 0)
                    }

                    else -> {
                        checkVisibilityButton(todoItemsState.visibility)
                    }
                }
            }
        }
    }

    private fun checkVisibilityButton(visibility: Boolean) {
        binding.isShowDone.backgroundTintList =
            ColorStateList.valueOf(requireContext().getColorFromTheme(R.attr.color_blue))
        if (visibility) {
            binding.isShowDone.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.visibility)
        } else {
            binding.isShowDone.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.visibility_off)
        }
    }

    private fun navigateToTodoItemFragmentWithEditMode(todoId: String) {
        val action = TodoItemsFragmentDirections.actionTodoItemsFragmentToTodoItemFragment(todoId)
        navController.navigate(action)
    }

    private fun navigateToTodoItemFragmentWithAddMode() {
        navigateToTodoItemFragmentWithEditMode("0")
    }
}