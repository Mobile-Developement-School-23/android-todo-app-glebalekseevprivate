package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.typography

@Composable
fun TodoEditTextField(
    initialText: String,
    onTextChanged: (String) -> Unit,
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
                bottom = 16.dp
            ),
        value = initialText,
        onValueChange = { onTextChanged(it) },
        textStyle = typography.body.copy(color = AppTheme.colors.labelPrimaryColor),
        minLines = 3,
        cursorBrush = SolidColor(AppTheme.colors.labelPrimaryColor)
    ) { innerTextField ->
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colors.backSecondaryColor,
                contentColor = AppTheme.colors.labelTertiaryColor
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                innerTextField.invoke()
                if (initialText.isEmpty()) {
                    Text(
                        text = stringResource(id = com.glebalekseevjk.design.R.string.example_todo_hint),
                        style = typography.body.copy(color = AppTheme.colors.labelTertiaryColor)
                    )
                }
            }
        }
    }
}