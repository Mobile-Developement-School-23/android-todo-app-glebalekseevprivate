package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoRemoveImageButton

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TodoRemoveImageButtonPreview(
    @PreviewParameter(BooleanParameterProvider::class) bool: Boolean
){
    ThemePreview {
        TodoRemoveImageButton(
            {},
            bool
        )
    }
}