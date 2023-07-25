package com.glebalekseevjk.todo.todolist.presentation.fragment.controller

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.glebalekseevjk.auth.domain.entity.NightMode
import com.glebalekseevjk.core.utils.di.OnBackPressed
import com.glebalekseevjk.core.utils.getColorFromTheme
import com.glebalekseevjk.core.utils.showToast
import com.glebalekseevjk.todo.todolist.presentation.R
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import com.glebalekseevjk.todo.todolist.presentation.databinding.DeletionSnackbarBinding
import com.glebalekseevjk.todo.todolist.presentation.databinding.FragmentTodoItemsBinding
import com.glebalekseevjk.todo.todolist.presentation.databinding.NotificationSnackbarBinding
import com.glebalekseevjk.todo.todolist.presentation.di.NavigateToTodoEdit
import com.glebalekseevjk.todo.todolist.presentation.fragment.TodoItemsFragment
import com.glebalekseevjk.todo.todolist.presentation.rv.adapter.TodoItemsAdapter
import com.glebalekseevjk.todo.todolist.presentation.rv.callback.SwipeCallback
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.ModalBottomSheetState
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.NotificationType
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.TodoItemsAction
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.TodoItemsState
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.TodoItemsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.math.pow


/**
 Этот класс отвечает за управление вьюшками во фрагменте TodoItemsFragment
 */

class TodoItemsViewController @Inject constructor(
    private val fragment: TodoItemsFragment,
    private val context: Context,
    private val rootView: View,
    private val binding: FragmentTodoItemsBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val todoItemsViewModel: TodoItemsViewModel,
    private val todoItemsAdapter: TodoItemsAdapter,
    private val todoItemRepository: TodoItemRepository,
    @NavigateToTodoEdit
    private val navigateToTodoEdit: @JvmSuppressWildcards (String, NavController) -> Unit
) : LifecycleOwner by viewLifecycleOwner {
    private val resources = context.resources
    private var isShowDoneMutex = Mutex()

    fun setupViews() {
        initListeners()
        initUI()
        initBottomSheet()
        initBottomSheetBehavior()
        initAppBar()
        setupRecyclerView()
        observeTodoItemsState()
        observeModalBottomSheetState()
        initTodoItemsState()
        observeNotifications()
        observeDeletionNotification()
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
            CoroutineScope(Dispatchers.Default).launch {
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

    private fun initBottomSheet() {
        binding.bottomSheetInclude.applyBtn.setOnClickListener {
            todoItemsViewModel.dispatch(
                TodoItemsAction.SaveNightMode(
                beforeSave = {
                    todoItemsViewModel.dispatch(TodoItemsAction.HideModalBottomSheet)
                }
            ))
        }
        binding.bottomSheetInclude.themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (todoItemsViewModel.modalBottomSheetState.value !is ModalBottomSheetState.Hidden) {
                val newNightMode = when (checkedId) {
                    R.id.radio_day -> NightMode.DAY
                    R.id.radio_night -> NightMode.NIGHT
                    R.id.radio_system -> NightMode.SYSTEM
                    else -> throw RuntimeException("No radio button checked")
                }
                todoItemsViewModel.dispatch(TodoItemsAction.SetNightMode(newNightMode))
            }
        }
    }

    private fun initBottomSheetBehavior() {
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(binding.bottomSheet)

        binding.darkOverlay.setOnClickListener {
            todoItemsViewModel.dispatch(TodoItemsAction.HideModalBottomSheet)
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    if (todoItemsViewModel.modalBottomSheetState.value !is ModalBottomSheetState.Hidden) {
                        todoItemsViewModel.dispatch(TodoItemsAction.HideModalBottomSheet)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun initAppBar() {
        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val relativePosition =
                (appBarLayout.totalScrollRange + verticalOffset).toFloat() / appBarLayout.totalScrollRange
            val alpha = relativePosition.toDouble().pow(3.0).toFloat()
            binding.countDoneTodoTv.alpha = if (alpha < 0.1) 0f else alpha

            updateMenuIconSize(interpolator(relativePosition))
        }
        binding.toolbar.menu.findItem(R.id.settings)
            .setOnMenuItemClickListener {
                todoItemsViewModel.dispatch(TodoItemsAction.ShowSettingsModalBottomSheet)
                true
            }
    }

    private var onBackPressedCallback: OnBackPressedCallback? = null
    private fun setOnBackPressedCallback(isDefault: Boolean) {
        if (isDefault) {
            onBackPressedCallback?.remove()
            onBackPressedCallback = null
        } else if (onBackPressedCallback == null) onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (todoItemsViewModel.modalBottomSheetState.value !is ModalBottomSheetState.Hidden) {
                        todoItemsViewModel.dispatch(TodoItemsAction.HideModalBottomSheet)
                    }
                }
            }.also {
                fragment.requireActivity().onBackPressedDispatcher.addCallback(
                    viewLifecycleOwner,
                    it
                )
            }
    }

    private fun interpolator(x: Float): Float {
        val boundary = 0.3f
        return if (x < boundary) 0f else {
            ((x - boundary) / (1f - boundary))
        }

    }

    private fun updateMenuIconSize(relativePosition: Float) {
        binding.toolbar.menu.findItem(R.id.settings).apply {
            icon?.alpha = (relativePosition * 255).toInt()
            val isEnabled = relativePosition != 0f
            if (this.isEnabled != isEnabled) this.isEnabled = isEnabled
        }
    }


    private fun initTodoItemsState() {
        lifecycleScope.launch {
            todoItemsViewModel.dispatch(TodoItemsAction.Init)
        }
    }

    private fun setupRecyclerView() {
        val swipeCallback = SwipeCallback(resources.getDimension(com.glebalekseevjk.design.R.dimen.swipe_backlash))
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
                            resources.getString(com.glebalekseevjk.design.R.string.count_done),
                            todoItemsState.countDone
                        )
                        binding.srl.isRefreshing = false
                        checkVisibilityButton(todoItemsState.visibility)
                    }

                    is TodoItemsState.Init -> {
                        binding.countDoneTodoTv.text =
                            String.format(resources.getString(com.glebalekseevjk.design.R.string.count_done), 0)
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

    private fun observeModalBottomSheetState() {
        lifecycleScope.launch {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            todoItemsViewModel.modalBottomSheetState.collectLatest {
                when (val state = it) {
                    is ModalBottomSheetState.Hidden -> {
                        setOnBackPressedCallback(isDefault = true)
                        binding.darkOverlay.visibility = View.GONE
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }

                    is ModalBottomSheetState.ShownSettings -> {
                        setOnBackPressedCallback(isDefault = false)
                        binding.darkOverlay.visibility = View.VISIBLE
                        with(binding.bottomSheetInclude) {
                            when (state.nightMode) {
                                NightMode.NIGHT -> radioNight.isChecked = true
                                NightMode.DAY -> radioDay.isChecked = true
                                NightMode.SYSTEM -> radioSystem.isChecked = true
                            }
                        }
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }
    }

    private fun checkVisibilityButton(visibility: Boolean) {
        val drawable = if (visibility) {
            AppCompatResources.getDrawable(fragment.requireContext(), com.glebalekseevjk.design.R.drawable.visibility)
        } else {
            AppCompatResources.getDrawable(fragment.requireContext(), com.glebalekseevjk.design.R.drawable.visibility_off)
        }
        binding.isShowDone.setImageDrawable(drawable)
        binding.isShowDone.backgroundTintList =
            ColorStateList.valueOf(context.getColorFromTheme(com.glebalekseevjk.design.R.attr.color_blue))
    }

    private fun navigateToTodoItemFragmentWithEditMode(todoId: String) {
        navigateToTodoEdit.invoke(todoId, fragment.findNavController())
    }

    private fun navigateToTodoItemFragmentWithAddMode() {
        navigateToTodoItemFragmentWithEditMode("0")
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            for (notification in todoItemsViewModel.notificationChannel) {
                when (notification) {
                    NotificationType.Unknown -> rootView.showNotificationSnackBar(resources.getString(com.glebalekseevjk.design.R.string.unknown_exception))
                    NotificationType.Client -> rootView.showNotificationSnackBar(resources.getString(com.glebalekseevjk.design.R.string.client_exception))
                    NotificationType.Connection -> rootView.showNotificationSnackBar(resources.getString(com.glebalekseevjk.design.R.string.connection_exception))
                    NotificationType.Server -> rootView.showNotificationSnackBar(resources.getString(com.glebalekseevjk.design.R.string.server_exception))
                    NotificationType.Unauthorized -> context.showToast(
                        resources.getString(
                            com.glebalekseevjk.design.R.string.unauthorized_exception
                        )
                    )
                }
            }
        }
    }

    private fun View.showNotificationSnackBar(message: String) {
        val snackBar = Snackbar.make(this, "", BaseTransientBottomBar.LENGTH_SHORT)
        val layoutParams = snackBar.view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin,
            layoutParams.rightMargin,
            layoutParams.bottomMargin + 40
        )
        snackBar.view.layoutParams = layoutParams
        val inflater = LayoutInflater.from(snackBar.context)
        val binding = NotificationSnackbarBinding.inflate(inflater)
        binding.messageTv.text = message
        (snackBar.view as Snackbar.SnackbarLayout).addView(binding.root, 0)
        snackBar.show()
    }

    private fun observeDeletionNotification() {
        lifecycleScope.launch {
            for (name in todoItemRepository.deletionNotification){
                val message = String.format(resources.getString(com.glebalekseevjk.design.R.string.cancel_deletion), name)

                val snackBar = Snackbar.make(rootView, "", 5000)
                val layoutParams = snackBar.view.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin + 40
                )
                snackBar.view.layoutParams = layoutParams
                val inflater = LayoutInflater.from(snackBar.context)
                val binding = DeletionSnackbarBinding.inflate(inflater)

                binding.cancelBtn.setOnClickListener {
                    lifecycleScope.launch {
                        todoItemRepository.cancelDeletion()
                        snackBar.dismiss()
                    }
                }
                binding.messageTv.text = message
                (snackBar.view as Snackbar.SnackbarLayout).addView(binding.root, 0)
                snackBar.show()
                lifecycleScope.launch {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.progressConstraintLayout)
                    val animator = ValueAnimator.ofFloat(0f, 1f)
                    animator.duration = 5000
                    animator.addUpdateListener { animation ->
                        val value = animation.animatedValue as Float
                        constraintSet.constrainPercentWidth(binding.progressView.id, value)
                        constraintSet.applyTo(binding.progressConstraintLayout)
                    }
                    animator.start()
                }
            }
        }
    }
}