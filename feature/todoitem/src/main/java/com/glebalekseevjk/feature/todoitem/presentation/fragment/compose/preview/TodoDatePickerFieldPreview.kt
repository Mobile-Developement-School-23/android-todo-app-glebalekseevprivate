package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component.TodoDatePickerField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


private class DateParameterProvider :
    PreviewParameterProvider<Date?> {
    override val values: Sequence<Date?>
        get() = sequenceOf(
            null,
            Calendar.getInstance().time
        )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TodoDatePickerFieldPreview(
    @PreviewParameter(DateParameterProvider::class) date: Date?
) {
    ThemePreview {
        TodoDatePickerField(
            date,
            {},
            {},
            {},
            SimpleDateFormat("dd.MM.yyyy"),
            SimpleDateFormat("HH:mm"),
        )
    }
}