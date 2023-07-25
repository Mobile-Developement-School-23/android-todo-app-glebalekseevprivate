package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.preview

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.AppTheme

@Composable
fun ThemePreview(
    content: @Composable () -> Unit
) {
    AppTheme {
        Surface(
            color = AppTheme.colors.backPrimaryColor
        ) {
            content()
        }
    }
}