package com.glebalekseevjk.feature.todoitem.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.glebalekseevjk.feature.todoitem.di.TodoItemFragmentSubcomponent
import com.glebalekseevjk.feature.todoitem.di.module.DateFormatter
import com.glebalekseevjk.feature.todoitem.di.module.TimeFormatter
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.TodoItemPage
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

/**
Ответственность класса TodoItemFragment:
Отображение и управление фрагментом для отдельного элемента списка задач (TodoItem).
Класс отвечает за создание и управление пользовательским интерфейсом,
включая связывание данных с макетом, обработку событий и взаимодействие с ViewModel.
 */
class TodoItemFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoItemViewModel: TodoItemViewModel
    private var fragmentComponent: TodoItemFragmentSubcomponent? = null
    private val args: TodoItemFragmentArgs by navArgs()

    @Inject
    @DateFormatter
    lateinit var dateFormatter: SimpleDateFormat

    @Inject
    @TimeFormatter
    lateinit var timeFormatter: SimpleDateFormat

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent =
            TodoItemsViewModel.fragmentComponent!!
                .createTodoItemFragmentSubcomponent()
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
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(requireContext()) {
                    TodoItemPage(
                        viewModel = todoItemViewModel,
                        dateFormatter = dateFormatter,
                        timeFormatter = timeFormatter,
                        onBackPressed = { findNavController().popBackStack() },
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        startPostponedEnterTransition()
    }

    private fun parseParams() {
        lifecycleScope.launch {
            todoItemViewModel.dispatch(TodoItemAction.Init(args.todoId))
        }
    }
}