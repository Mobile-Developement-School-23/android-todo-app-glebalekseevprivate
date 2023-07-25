package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.component.TodoImportanceField
import com.glebalekseevjk.todo.domain.entity.TodoItem

class ImportanceParameterProvider :
    PreviewParameterProvider<TodoItem.Companion.Importance> {
    override val values: Sequence<TodoItem.Companion.Importance>
        get() = sequenceOf(
            TodoItem.Companion.Importance.LOW,
            TodoItem.Companion.Importance.BASIC,
            TodoItem.Companion.Importance.IMPORTANT,
        )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TodoImportanceFieldPreview(
    @PreviewParameter(ImportanceParameterProvider::class) importance: TodoItem.Companion.Importance
) {
    ThemePreview {
        TodoImportanceField(
            importance,
            {}
        )
    }
}