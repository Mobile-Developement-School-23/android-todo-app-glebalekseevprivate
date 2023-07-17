package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.ChooseImportanceModalBottomSheet

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