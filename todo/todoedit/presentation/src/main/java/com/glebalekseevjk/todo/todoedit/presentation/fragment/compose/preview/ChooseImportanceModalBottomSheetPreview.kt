package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.ChooseImportanceModalBottomSheet
import com.glebalekseevjk.todo.domain.entity.TodoItem

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ChooseImportanceModalBottomSheetPreview(
    @PreviewParameter(ImportanceParameterProvider::class) importance: TodoItem.Companion.Importance
) {
    ThemePreview {
        ChooseImportanceModalBottomSheet(
            importance,
            {}
        )
    }
}