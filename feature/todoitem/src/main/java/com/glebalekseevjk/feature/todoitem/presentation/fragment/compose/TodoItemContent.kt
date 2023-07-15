package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoDatePickerField
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoEditTextField
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoImportanceField
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoRemoveImageButton
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemState
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun TodoItemContent(
    modifier: Modifier,
    listState: LazyListState,
    viewModel: TodoItemViewModel,
    dateFormatter: SimpleDateFormat,
    timeFormatter: SimpleDateFormat,
    onBackPressed: () -> Unit,
    onOpenImportanceBottomSheet: () -> Unit,
) {
    val viewStates = viewModel.viewStates
    val todoItem = viewStates.todoItem


    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        item {
            TodoEditTextField(
                initialText = todoItem.text,
                onTextChanged = { viewModel.dispatch(TodoItemAction.SetText(it)) },
            )
        }

        item {
            TodoImportanceField(
                onOpenImportanceBottomSheet = onOpenImportanceBottomSheet,
                importance = todoItem.importance
            )
        }

        item {
            Divider(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(
                        horizontal = 16.dp
                    )
                    .fillMaxWidth()
                    .height(0.5.dp),
                color = AppTheme.colors.supportSeparatorColor
            )
        }

        item {
            TodoDatePickerField(
                date = todoItem.deadline,
                onInitDeadline = {
                    val newDeadline =
                        Calendar.getInstance()
                            .apply { add(Calendar.WEEK_OF_YEAR, 2) }.time
                    viewModel.dispatch(TodoItemAction.SetDeadline(newDeadline))
                },
                onDisableDeadline = {
                    viewModel.dispatch(TodoItemAction.SetDeadline(null))
                },
                dateFormatter = dateFormatter,
                timeFormatter = timeFormatter,
                onSetDeadline = {
                    viewModel.dispatch(
                        TodoItemAction.SetDeadline(
                            it
                        )
                    )
                }
            )
        }

        item {
            Divider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(0.5.dp),
                color = AppTheme.colors.supportSeparatorColor
            )
        }

        item {
            TodoRemoveImageButton(
                onClick = { viewModel.dispatch(TodoItemAction.DeleteTodoItem); onBackPressed.invoke() },
                isEnabled = (viewStates as? TodoItemState.Loaded)?.isEdit ?: false,
            )
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}