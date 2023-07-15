package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.ChooseImportanceModalBottomSheet
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoModalBottomSheetLayout
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoItemPage(
    viewModel: TodoItemViewModel,
    dateFormatter: SimpleDateFormat,
    onBackPressed: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val topBarElevation by animateDpAsState(targetValue = if (listState.canScrollBackward) 12.dp else 0.dp)
    val scope = rememberCoroutineScope()

    TodoModalBottomSheetLayout(
        content = {
            Scaffold(
                modifier = Modifier,
                scaffoldState = scaffoldState,
                backgroundColor = AppTheme.colors.backPrimaryColor,
                topBar = {
                    TodoItemTopBar(
                        onBackPressed = onBackPressed,
                        viewModel = viewModel,
                        topBarElevation = topBarElevation
                    )
                },
                content = {
                    TodoItemContent(
                        modifier = Modifier.padding(it),
                        onBackPressed = onBackPressed,
                        viewModel = viewModel,
                        dateFormatter = dateFormatter,
                        listState = listState,
                        onOpenImportanceBottomSheet = {
                            scope.launch {
                                modalBottomSheetState.show()
                            }
                        }
                    )
                },
            )
        },
        bottomSheetState = modalBottomSheetState,
        sheetContent = {
            ChooseImportanceModalBottomSheet(
                initValue = viewModel.viewStates.todoItem.importance,
                onItemSelected = {
                    viewModel.dispatch(TodoItemAction.SetImportance(it))
                })
        },
    )
}

