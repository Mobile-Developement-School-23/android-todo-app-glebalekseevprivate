package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.design.R
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.SmallIconSize
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.transparent
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.typography
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemAction
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemState
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel

@Composable
fun TodoItemTopBar(
    viewModel: TodoItemViewModel,
    topBarElevation: Dp,
    onBackPressed: () -> Unit,

) {
    val viewStates = viewModel.viewStates

    Row(
        modifier = Modifier
            .shadow(topBarElevation)
            .background(AppTheme.colors.backPrimaryColor)
            .fillMaxWidth()
            .padding(
                start = 4.dp,
                end = 4.dp,
                top = 16.dp,
                bottom = 0.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.close),
                modifier = Modifier
                    .padding(5.dp)
                ,
                tint = AppTheme.colors.labelPrimaryColor,
                contentDescription = stringResource(id = R.string.close)
            )
        }

        Button(
            onClick = {
                if (viewStates is TodoItemState.Loaded) viewModel.dispatch(TodoItemAction.SaveTodoItem)
                onBackPressed()
            },
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = transparent,
                contentColor = AppTheme.colors.colorBlue
            )
        ) {
            Text(
                text = stringResource(id = R.string.save_text),
                style = typography.button,
                color = AppTheme.colors.colorBlue,
                modifier = Modifier
            )
        }



    }
}