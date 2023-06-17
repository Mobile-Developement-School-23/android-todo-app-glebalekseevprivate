package com.glebalekseevjk.todoapp.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemBinding
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemState
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.todoapp.utils.getColorFromTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale


class TodoItemFragment : Fragment() {
    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemBinding is null")

    private val args: TodoItemFragmentArgs by navArgs()
    private lateinit var todoViewModel: TodoItemViewModel
    private lateinit var navController: NavController
    private val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
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
        super.onViewCreated(view, savedInstanceState)
        initNavigationUI()
        initDatePicker()
        initListeners()
        setupToolbar()
        observeTodoItemState()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        todoViewModel.dispatch(TodoItemAction.Init(args.todoId))
    }

    private fun initNavigationUI() {
        navController = findNavController()
    }

    private fun initDatePicker() {
        datePickerDialog = DatePickerDialog(requireActivity())
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_POSITIVE, "готово",
            datePickerDialog
        )
        datePickerDialog.setOnDateSetListener { datePicker, _, _, _ ->
            val deadline = GregorianCalendar(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            ).time
            todoViewModel.dispatch(
                TodoItemAction.SetDeadline(
                    deadline
                )
            )
            binding.deadlineDateTv.text = formatter.format(deadline)
        }
    }

    private fun initListeners() {
        binding.textEt.addTextChangedListener {
            todoViewModel.dispatch(TodoItemAction.SetText(it.toString()))
        }
        binding.deleteBtn.setOnClickListener {
            todoViewModel.dispatch(TodoItemAction.DeleteTodoItem)
            navController.popBackStack()
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                todoViewModel.dispatch(
                    TodoItemAction.SetImportance(
                        when (selectedItemPosition) {
                            0 -> TodoItem.Companion.Importance.LOW
                            1 -> TodoItem.Companion.Importance.NORMAL
                            2 -> TodoItem.Companion.Importance.IMPORTANT
                            else -> throw RuntimeException("Unknown importance")
                        }
                    )
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.deadlineSw.setOnCheckedChangeListener { buttonView, isChecked ->
            when (val todoItemState = todoViewModel.todoItemState.value) {
                is TodoItemState.Init -> {}
                is TodoItemState.Loaded -> {
                    if (isChecked) {
                        binding.deadlineDateTv.visibility = View.VISIBLE
                        if (todoItemState.todoItem.deadline == null) {
                            val newDeadline =
                                Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, 2) }.time
                            todoViewModel.dispatch(TodoItemAction.SetDeadline(newDeadline))
                            binding.deadlineDateTv.text = formatter.format(newDeadline)
                        } else {
                            binding.deadlineDateTv.text =
                                todoItemState.todoItem.deadline?.let { formatter.format(it) }
                        }
                        datePickerDialog.show()
                    } else {
                        binding.deadlineDateTv.visibility = View.INVISIBLE
                        todoViewModel.dispatch(TodoItemAction.SetDeadline(null))
                    }
                }
            }
        }
        binding.deadlineDateTv.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun setupToolbar() {
        with(binding.toolbar) {
            setNavigationOnClickListener {
                navController.popBackStack()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save_todo -> {
                        when (val todoItemState = todoViewModel.todoItemState.value) {
                            is TodoItemState.Init -> {}
                            is TodoItemState.Loaded -> {
                                todoViewModel.dispatch(TodoItemAction.SaveTodoItem)
                            }
                        }
                        navController.popBackStack()
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
                    binding.deadlineSw.isChecked = todoItemState.todoItem.deadline != null
                    binding.deadlineDateTv.visibility =
                        if (todoItemState.todoItem.deadline != null) View.VISIBLE else View.INVISIBLE
                    binding.deadlineDateTv.text =
                        todoItemState.todoItem.deadline?.let { formatter.format(it) }
                    when (todoItemState) {
                        is TodoItemState.Init -> {}
                        is TodoItemState.Loaded -> {
                            if (!todoItemState.isEdit) {
                                binding.deleteBtn.isEnabled = false
                                binding.deleteBtn.setTextColor(requireContext().getColorFromTheme(R.attr.label_disable))
                                binding.deleteBtn.compoundDrawableTintList =
                                    ColorStateList.valueOf(requireContext().getColorFromTheme(R.attr.label_disable))
                            }
                            scope.cancel()
                        }
                    }
                }
            }
        }
    }
}