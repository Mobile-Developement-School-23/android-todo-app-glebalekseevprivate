package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoEditTextField


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TodoEditTextFieldPreview() {
    ThemePreview {
        TodoEditTextField(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            {}
        )
    }
}