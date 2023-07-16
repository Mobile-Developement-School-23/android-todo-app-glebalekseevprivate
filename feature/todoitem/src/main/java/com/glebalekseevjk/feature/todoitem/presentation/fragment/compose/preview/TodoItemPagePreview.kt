package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.TodoItemPage
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.ITodoItemViewModel
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID

data class TodoItemPageTestCase(
    val isEdit: Boolean,
    val todoItem: TodoItem
)


class TodoItemPageTestCaseParameterProvider :
    PreviewParameterProvider<TodoItemPageTestCase> {
    override val values: Sequence<TodoItemPageTestCase>
        get() = sequenceOf(
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    TodoItem.Companion.Importance.LOW,
                    deadline = Calendar.getInstance().time,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = true
            ),
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    TodoItem.Companion.Importance.LOW,
                    deadline = Calendar.getInstance().time,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = false
            ),
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    TodoItem.Companion.Importance.LOW,
                    deadline = null,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = false
            ),
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    TodoItem.Companion.Importance.IMPORTANT,
                    deadline = null,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = false
            ),
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    TodoItem.Companion.Importance.BASIC,
                    deadline = null,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = false
            ),
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem.",
                    TodoItem.Companion.Importance.BASIC,
                    deadline = null,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = false
            ),
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    TodoItem.Companion.Importance.BASIC,
                    deadline = null,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = false
            ),
            TodoItemPageTestCase(
                todoItem = TodoItem(
                    UUID.randomUUID().toString(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    TodoItem.Companion.Importance.BASIC,
                    deadline = null,
                    isDone = true,
                    createdAt = Calendar.getInstance().time,
                    changedAt = Calendar.getInstance().time
                ),
                isEdit = false
            ),
        )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TodoItemPagePreview(
    @PreviewParameter(TodoItemPageTestCaseParameterProvider::class) todoItemPageTestCase: TodoItemPageTestCase,
) {
    AppTheme {
        TodoItemPage(
            object : ITodoItemViewModel {
                override val viewStates: TodoItemState
                    get() = TodoItemState.Loaded(
                        todoItemPageTestCase.todoItem,
                        todoItemPageTestCase.isEdit
                    )

                override fun dispatch(action: TodoItemAction) {

                }
            },
            SimpleDateFormat("dd.MM.yyyy"),
            SimpleDateFormat("HH:mm"),
            {}
        )
    }
}