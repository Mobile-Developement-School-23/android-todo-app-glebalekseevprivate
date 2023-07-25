package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.design.R
import com.glebalekseevjk.todo.domain.entity.TodoItem.Companion.Importance
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.typography

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TodoImportanceField(
    importance: Importance,
    onOpenImportanceBottomSheet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Button(
            onClick = onOpenImportanceBottomSheet,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppTheme.colors.backPrimaryColor,
                contentColor = AppTheme.colors.labelTertiaryColor,
            ),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(id = R.string.importance),
                    style = typography.body.copy(color = AppTheme.colors.labelPrimaryColor)
                )

                Spacer(modifier = Modifier.height(4.dp))

                AnimatedContent(
                    modifier = Modifier,
                    contentAlignment = Alignment.CenterStart,
                    targetState = importance,
                ) {
                    val importanceArray = stringArrayResource(id = R.array.importance_array)
                    val isImportant = importance == Importance.IMPORTANT

                    Text(
                        text = when (importance) {
                            Importance.LOW -> importanceArray[1]
                            Importance.BASIC -> importanceArray[0]
                            Importance.IMPORTANT -> importanceArray[2]
                        },
                        style = typography.subhead.copy(
                            color = if (isImportant) AppTheme.colors.colorRed
                            else AppTheme.colors.labelTertiaryColor
                        )
                    )
                }
            }
        }
    }
}