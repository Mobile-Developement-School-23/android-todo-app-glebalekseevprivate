package com.glebalekseevjk.feature.todoitem.presentation.fragment.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.glebalekseevjk.core.utils.getColorFromTheme
import com.glebalekseevjk.design.R
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.feature.todoitem.databinding.FragmentTodoItemBinding
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemState
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

class TodoItemViewController @Inject constructor(
    private val context: Context,
    private val activity: Activity,
    private val binding: FragmentTodoItemBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val todoViewModel: TodoItemViewModel,
    private val navController: NavController,
    private val formatter: SimpleDateFormat
) : LifecycleOwner by lifecycleOwner {
    private val resources = context.resources
    private lateinit var datePickerDialog: DatePickerDialog

    fun setupViews() {
        initDatePicker()
        observeTodoItemState()
        initListeners()
        setupToolbar()
    }

    private fun initDatePicker() {
        datePickerDialog = DatePickerDialog(activity)
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_POSITIVE, resources.getString(R.string.ready),
            datePickerDialog
        )
        datePickerDialog.setOnDateSetListener { datePicker, _, _, _ ->
            val deadline = GregorianCalendar(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            ).time
            lifecycleScope.launchWhenResumed {
                todoViewModel.dispatch(
                    TodoItemAction.SetDeadline(
                        deadline
                    )
                )
                binding.deadlineDateTv.text = formatter.format(deadline)
            }
        }
    }

    private fun initListeners() {
        binding.deleteBtn.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                todoViewModel.dispatch(TodoItemAction.DeleteTodoItem)
                navController.popBackStack()
            }
        }
        binding.importantTv.setOnClickListener {
            binding.spinner.performClick()
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                lifecycleScope.launchWhenResumed {
                    todoViewModel.dispatch(
                        TodoItemAction.SetImportance(
                            when (selectedItemPosition) {
                                1 -> TodoItem.Companion.Importance.LOW
                                0 -> TodoItem.Companion.Importance.BASIC
                                2 -> TodoItem.Companion.Importance.IMPORTANT
                                else -> throw RuntimeException("Unknown importance")
                            }
                        )
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.deadlineSw.setOnCheckedChangeListener { buttonView, isChecked ->
            when (val todoItemState = todoViewModel.todoItemState.value) {
                is TodoItemState.Init -> {}
                is TodoItemState.Loaded -> {
                    lifecycleScope.launchWhenResumed {
                        if (isChecked) {
                            binding.deadlineDateTv.visibility = View.VISIBLE
                            if (todoItemState.todoItem.deadline == null) {
                                val newDeadline =
                                    Calendar.getInstance()
                                        .apply { add(Calendar.WEEK_OF_YEAR, 2) }.time
                                todoViewModel.dispatch(TodoItemAction.SetDeadline(newDeadline))
                                binding.deadlineDateTv.text = formatter.format(newDeadline)
                            } else {
                                binding.deadlineDateTv.text =
                                    todoItemState.todoItem.deadline?.let { formatter.format(it) }
                            }
                        } else {
                            binding.deadlineDateTv.visibility = View.INVISIBLE
                            todoViewModel.dispatch(TodoItemAction.SetDeadline(null))
                        }
                    }
                }
            }
        }
        binding.deadlineDateTv.setOnClickListener {
            datePickerDialog.show()
            todoViewModel.todoItemState.value.todoItem.deadline?.let {
                val calendar = Calendar.getInstance()
                calendar.time = it
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                datePickerDialog.updateDate(year, month, day)
            }
        }
    }

    private fun setupToolbar() {
        with(binding.toolbar) {
            setNavigationOnClickListener {
                navController.popBackStack()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    com.glebalekseevjk.feature.todoitem.R.id.save_todo -> {
                        lifecycleScope.launchWhenCreated {
                            when (todoViewModel.todoItemState.value) {
                                is TodoItemState.Init -> {}
                                is TodoItemState.Loaded -> {
                                    todoViewModel.dispatch(TodoItemAction.SaveTodoItem)
                                }
                            }
                            navController.popBackStack()
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun observeTodoItemState() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            todoViewModel.todoItemState.collect { todoItemState ->
                withContext(Dispatchers.Main) {
                    binding.textEt.setText(todoItemState.todoItem.text)
                    if (todoItemState !is TodoItemState.Init) {
                        binding.textEt.addTextChangedListener {
                            todoViewModel.dispatch(TodoItemAction.SetText(it.toString()))
                        }
                    }
                    binding.deadlineSw.isChecked = todoItemState.todoItem.deadline != null
                    binding.deadlineDateTv.visibility =
                        if (todoItemState.todoItem.deadline != null) View.VISIBLE else View.INVISIBLE
                    binding.deadlineDateTv.text =
                        todoItemState.todoItem.deadline?.let { formatter.format(it) }
                    binding.spinner.setSelection(
                        when (todoItemState.todoItem.importance) {
                            TodoItem.Companion.Importance.LOW -> 1
                            TodoItem.Companion.Importance.BASIC -> 0
                            TodoItem.Companion.Importance.IMPORTANT -> 2
                        }
                    )
                    when (todoItemState) {
                        is TodoItemState.Init -> {}
                        is TodoItemState.Loaded -> {
                            if (!todoItemState.isEdit) {
                                binding.deleteBtn.isEnabled = false
                                binding.deleteBtn.setTextColor(context.getColorFromTheme(R.attr.label_disable))
                                binding.deleteBtn.compoundDrawableTintList =
                                    ColorStateList.valueOf(context.getColorFromTheme(R.attr.label_disable))
                            } else {
                                binding.deleteBtn.isEnabled = true
                                binding.deleteBtn.setTextColor(context.getColorFromTheme(R.attr.color_red))
                                binding.deleteBtn.compoundDrawableTintList =
                                    ColorStateList.valueOf(context.getColorFromTheme(R.attr.color_red))
                            }
                            scope.cancel()
                        }
                    }
                }
            }
        }
    }
}