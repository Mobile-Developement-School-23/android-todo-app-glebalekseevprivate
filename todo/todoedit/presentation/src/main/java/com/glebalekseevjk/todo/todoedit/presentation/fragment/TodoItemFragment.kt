package com.glebalekseevjk.todo.todoedit.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.di.OnBackPressed
import com.glebalekseevjk.core.utils.di.findDependencies
import com.glebalekseevjk.todo.todoedit.presentation.di.DaggerTodoEditComponent
import com.glebalekseevjk.todo.todoedit.presentation.di.module.DateFormatter
import com.glebalekseevjk.todo.todoedit.presentation.di.module.TimeFormatter
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.TodoItemPage
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.todo.todoedit.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.todo.todoedit.presentation.viewmodel.TodoItemViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject


/**
 Этот класс отвечает за управление фрагментом, отображающим задачу.
 */
class TodoItemFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoItemViewModel: TodoItemViewModel
    private val args: TodoItemFragmentArgs by navArgs()

    @Inject
    @DateFormatter
    lateinit var dateFormatter: SimpleDateFormat

    @Inject
    @TimeFormatter
    lateinit var timeFormatter: SimpleDateFormat

    @Inject
    lateinit var personalUseCase: PersonalUseCase

    override fun onAttach(context: Context) {
        super.onAttach(context)
            DaggerTodoEditComponent
             .factory()
             .create(findDependencies())
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
        val nightMode = personalUseCase.nightMode
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(
                    nightMode
                ) {
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

    companion object {
        const val TODO_ID = "todo_id"
        fun newInstance(todoId: String): TodoItemFragment {
            val args = Bundle()
            args.putString(TODO_ID, todoId)
            val fragment = TodoItemFragment()
            fragment.arguments = args
            return fragment
        }
    }
}