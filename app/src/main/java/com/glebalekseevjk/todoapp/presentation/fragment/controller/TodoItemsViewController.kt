package com.glebalekseevjk.todoapp.presentation.fragment.controller

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.todoapp.presentation.fragment.TodoItemsFragmentDirections
import com.glebalekseevjk.todoapp.presentation.rv.adapter.TodoItemsAdapter
import com.glebalekseevjk.todoapp.presentation.rv.callback.SwipeCallback
import com.glebalekseevjk.todoapp.presentation.viewmodel.NotificationType
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsAction
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsState
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsViewModel
import com.glebalekseevjk.todoapp.utils.getColorFromTheme
import com.glebalekseevjk.todoapp.utils.showSnackbar
import com.glebalekseevjk.todoapp.utils.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.math.pow


class TodoItemsViewController @Inject constructor(
    private val context: Context,
    private val rootView: View,
    private val binding: FragmentTodoItemsBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val todoItemsViewModel: TodoItemsViewModel,
    private val todoItemsAdapter: TodoItemsAdapter,
    private val navController: NavController
) : LifecycleOwner by lifecycleOwner {
    private val resources = context.resources
    private var isShowDoneMutex = Mutex()

    fun setupViews() {
        initListeners()
        initUI()
        initAppBar()
        setupRecyclerView()
        observeTodoItemsState()
        initTodoItemsState()
        observeNotifications()
    }

    private fun initListeners() {
        binding.addTodoBtn.setOnClickListener {
            navigateToTodoItemFragmentWithAddMode()
        }
        binding.isShowDone.setOnClickListener {
            if (isShowDoneMutex.isLocked) return@setOnClickListener
            lifecycleScope.launch {
                isShowDoneMutex.withLock {
                    todoItemsViewModel.dispatch(TodoItemsAction.ChangeVisibility)
                    delay(400)
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            lifecycleScope.launch {
                todoItemsViewModel.dispatch(TodoItemsAction.Quit)
            }
        }
        binding.srl.setOnRefreshListener {
            lifecycleScope.launch {
                todoItemsViewModel.dispatch(TodoItemsAction.PullToRefresh)
            }
        }
    }

    private fun initUI() {
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
        val swipeCallback = SwipeCallback(resources.getDimension(R.dimen.swipe_backlash))
        with(binding.todoListRv) {
            adapter = todoItemsAdapter
            ItemTouchHelper(swipeCallback).attachToRecyclerView(this)
        }
        todoItemsAdapter.editClickListener = { todoId ->
            navigateToTodoItemFragmentWithEditMode(todoId)
        }
        todoItemsAdapter.changeDoneStatusClickListener = { todoId ->
            lifecycleScope.launch {
                todoItemsViewModel.dispatch(
                    TodoItemsAction.ChangeDoneStatus(
                        todoId,
                        context
                    )
                )
            }
        }
        todoItemsAdapter.setDoneStatusClickListener = { todoId, viewHolder ->
            swipeCallback.resetViewHolder(viewHolder)
            lifecycleScope.launch {
                todoItemsViewModel.dispatch(TodoItemsAction.SetDoneStatus(todoId, context))

            }
        }
        todoItemsAdapter.addTodoItemClickListener = {
            navigateToTodoItemFragmentWithAddMode()
        }
        todoItemsAdapter.deleteTodoItemClickListener = { todoId, viewHolder ->
            lifecycleScope.launch {
                swipeCallback.resetViewHolder(viewHolder)
                todoItemsViewModel.dispatch(
                    TodoItemsAction.DeleteTodoItem(
                        todoId,
                        context
                    )
                )
            }
        }
    }

    private fun observeTodoItemsState() {
        lifecycleScope.launch {
            todoItemsViewModel.todoItemsState.collect { todoItemsState ->
                binding.lastSyncDateTv.text = todoItemsState.lastSyncDate
                when (todoItemsState) {
                    is TodoItemsState.Loaded -> {
                        if (todoItemsState.todoItemsDisplay.isEmpty()) {
                            binding.materialCardView.visibility = View.INVISIBLE
                            binding.itemsNotFoundTv.visibility = View.VISIBLE
                        } else {
                            binding.materialCardView.visibility = View.VISIBLE
                            binding.itemsNotFoundTv.visibility = View.INVISIBLE
                        }
                        todoItemsAdapter.submitList(todoItemsState.todoItemsDisplay)
                        binding.countDoneTodoTv.text = String.format(
                            resources.getString(R.string.count_done),
                            todoItemsState.countDone
                        )
                        binding.srl.isRefreshing = false
                        checkVisibilityButton(todoItemsState.visibility)
                        binding.todoListRv.scrollToPosition(0)
                    }

                    is TodoItemsState.Init -> {
                        binding.countDoneTodoTv.text =
                            String.format(resources.getString(R.string.count_done), 0)
                        binding.materialCardView.visibility = View.INVISIBLE
                    }

                    is TodoItemsState.Loading -> {
                        checkVisibilityButton(todoItemsState.visibility)
                        binding.srl.isRefreshing = true
                    }
                }
            }
        }
    }

    private fun checkVisibilityButton(visibility: Boolean) {
        binding.isShowDone.backgroundTintList =
            ColorStateList.valueOf(context.getColorFromTheme(R.attr.color_blue))
        if (visibility) {
            binding.isShowDone.background =
                AppCompatResources.getDrawable(context, R.drawable.visibility)
        } else {
            binding.isShowDone.background =
                AppCompatResources.getDrawable(context, R.drawable.visibility_off)
        }
    }

    private fun navigateToTodoItemFragmentWithEditMode(todoId: String) {
        val action = TodoItemsFragmentDirections.actionTodoItemsFragmentToTodoItemFragment(todoId)
        navController.navigate(action)
    }

    private fun navigateToTodoItemFragmentWithAddMode() {
        navigateToTodoItemFragmentWithEditMode("0")
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            for (notification in todoItemsViewModel.notificationChannel) {
                when (notification) {
                    NotificationType.Unknown -> rootView.showSnackbar(resources.getString(R.string.unknown_exception))
                    NotificationType.Client -> rootView.showSnackbar(resources.getString(R.string.client_exception))
                    NotificationType.Connection -> rootView.showSnackbar(resources.getString(R.string.connection_exception))
                    NotificationType.Server -> rootView.showSnackbar(resources.getString(R.string.server_exception))
                    NotificationType.Unauthorized -> context.showToast(
                        resources.getString(
                            R.string.unauthorized_exception
                        )
                    )
                }
            }
        }
    }
}