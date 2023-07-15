package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.glebalekseevjk.design.R

data class Typography(
    val largeTitle: TextStyle,
    val title: TextStyle,
    val subhead: TextStyle,
    val body: TextStyle,
    val button: TextStyle,
)

val typography = Typography(
    largeTitle = TextStyle(
        fontSize = 32.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        lineHeight = 37.5.sp,
    ),
    title = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
    ),
    subhead = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        lineHeight = 20.sp,
    ),
    body = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        lineHeight = 20.sp,
    ),
    button = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        lineHeight = 24.sp,
        letterSpacing = 0.16.sp,
        textAlign = TextAlign.Center
    ),
)
