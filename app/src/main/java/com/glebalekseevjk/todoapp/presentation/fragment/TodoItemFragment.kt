package com.glebalekseevjk.todoapp.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.databinding.FragmentTodoItemBinding
import com.glebalekseevjk.todoapp.domain.entity.TodoItem.Companion.Importance.*
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemState
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.todoapp.utils.appComponent
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
import javax.inject.Inject


class TodoItemFragment : Fragment() {
    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val args: TodoItemFragmentArgs by navArgs()
    private lateinit var todoViewModel: TodoItemViewModel
    private lateinit var navController: NavController
    private lateinit var formatter: SimpleDateFormat
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.injectTodoItemFragment(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel = ViewModelProvider(this, viewModelFactory)[TodoItemViewModel::class.java]
        formatter = SimpleDateFormat(resources.getString(R.string.date_pattern), Locale("ru"))
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
        super.onViewCreated(view, savedInstanceState)
        initNavigationUI()
        initDatePicker()
        observeTodoItemState()
        initListeners()
        setupToolbar()
        startPostponedEnterTransition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        lifecycleScope.launch {
            todoViewModel.dispatch(TodoItemAction.Init(args.todoId))
        }
    }

    private fun initNavigationUI() {
        navController = findNavController()
    }

    private fun initDatePicker() {
        datePickerDialog = DatePickerDialog(requireActivity())
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
            lifecycleScope.launch {
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
            lifecycleScope.launch {
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
                lifecycleScope.launch {
                    todoViewModel.dispatch(
                        TodoItemAction.SetImportance(
                            when (selectedItemPosition) {
                                1 -> LOW
                                0 -> BASIC
                                2 -> IMPORTANT
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
                    lifecycleScope.launch {
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
                    R.id.save_todo -> {
                        lifecycleScope.launch {
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
                            LOW -> 1
                            BASIC -> 0
                            IMPORTANT -> 2
                        }
                    )
                    when (todoItemState) {
                        is TodoItemState.Init -> {}
                        is TodoItemState.Loaded -> {
                            if (!todoItemState.isEdit) {
                                binding.deleteBtn.isEnabled = false
                                binding.deleteBtn.setTextColor(requireContext().getColorFromTheme(R.attr.label_disable))
                                binding.deleteBtn.compoundDrawableTintList =
                                    ColorStateList.valueOf(requireContext().getColorFromTheme(R.attr.label_disable))
                            } else {
                                binding.deleteBtn.isEnabled = true
                                binding.deleteBtn.setTextColor(requireContext().getColorFromTheme(R.attr.color_red))
                                binding.deleteBtn.compoundDrawableTintList =
                                    ColorStateList.valueOf(requireContext().getColorFromTheme(R.attr.color_red))
                            }
                            scope.cancel()
                        }
                    }
                }
            }
        }
    }
}