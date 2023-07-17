package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.design.R
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.typography

@Composable
fun TodoRemoveImageButton(
    onClick: () -> Unit,
    isEnabled: Boolean = false
) {
    Button(
        enabled = isEnabled,
        onClick = onClick,
        elevation = null,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 23.dp, horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.backPrimaryColor,
            contentColor = AppTheme.colors.colorRed,
            disabledBackgroundColor = AppTheme.colors.backPrimaryColor,
            disabledContentColor = AppTheme.colors.labelDisableColor
        ),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                modifier = Modifier,
                contentDescription = stringResource(id = R.string.delete_text)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(id = R.string.delete_text),
                style = typography.body
            )
        }
    }
}