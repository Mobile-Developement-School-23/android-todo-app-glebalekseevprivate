package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme.MiddleCornerRadius

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoModalBottomSheetLayout(
    content: @Composable () -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
    bottomSheetState: ModalBottomSheetState
) {
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = sheetContent,
        sheetShape = RoundedCornerShape(
            topStart = MiddleCornerRadius,
            topEnd = MiddleCornerRadius
        ),
        sheetBackgroundColor = AppTheme.colors.backSecondaryColor,
        sheetContentColor = AppTheme.colors.backPrimaryColor,
        scrimColor = Color.Black.copy(alpha = 0.4f)
    ) {
        content.invoke()
    }
}