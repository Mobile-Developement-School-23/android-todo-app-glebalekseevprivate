package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.typography

@Preview
@Composable
fun TextStylePreview() {
    val padding = Modifier.padding(vertical = 12.dp, horizontal = 32.dp)
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(vertical = 20.dp),
    ) {
        Text(text = "Large title — 32/38", style = typography.largeTitle, modifier = padding)
        Text(text = "Title — 20/32", style = typography.title, modifier = padding)
        Text(text = "BUTTON — 14/24", style = typography.button, modifier = padding)
        Text(text = "Body — 16/20", style = typography.body, modifier = padding)
        Text(text = "Subhead — 14/20", style = typography.subhead, modifier = padding)
    }
}