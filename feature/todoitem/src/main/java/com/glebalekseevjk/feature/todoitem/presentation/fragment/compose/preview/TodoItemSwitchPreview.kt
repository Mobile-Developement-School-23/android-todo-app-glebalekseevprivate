package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoItemSwitch
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme

class BooleanParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TodoItemSwitchPreview(
    @PreviewParameter(BooleanParameterProvider::class) bool: Boolean
) {
    ThemePreview {
        TodoItemSwitch(
            modifier = Modifier,
            scale = 0.8f,
            checked = bool,
            checkedTrackColor = AppTheme.colors.colorBlue,
            uncheckedTrackColor = AppTheme.colors.colorBlue30
        )
    }
}