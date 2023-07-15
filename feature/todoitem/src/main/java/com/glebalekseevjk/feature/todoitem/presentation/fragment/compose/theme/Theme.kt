package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.theme

import android.content.Context
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.glebalekseevjk.design.R

val transparent = Color(0x00000000)

data class AppColors(
    val supportSeparatorColor: Color,
    val supportOverlayColor: Color,
    val labelPrimaryColor: Color,
    val labelSecondaryColor: Color,
    val labelTertiaryColor: Color,
    val labelDisableColor: Color,
    val colorRed: Color,
    val colorRed20: Color,
    val colorGreen: Color,
    val colorBlue: Color,
    val colorBlue30: Color,
    val colorGray: Color,
    val colorGrayLight: Color,
    val colorWhite: Color,
    val backPrimaryColor: Color,
    val backSecondaryColor: Color,
    val backElevatedColor: Color,
    val colorAccent: Color,
    val colorPrimary: Color
)

@Composable
fun AppTheme(
    context: Context,
    content: @Composable () -> Unit
) {
    val appColors = AppColors(
        supportSeparatorColor = context.resolveColorAttr(R.attr.support_separator),
        supportOverlayColor = context.resolveColorAttr(R.attr.support_overlay),
        labelPrimaryColor = context.resolveColorAttr(R.attr.label_primary),
        labelSecondaryColor = context.resolveColorAttr(R.attr.label_secondary),
        labelTertiaryColor = context.resolveColorAttr(R.attr.label_tertiary),
        labelDisableColor = context.resolveColorAttr(R.attr.label_disable),
        colorRed = context.resolveColorAttr(R.attr.color_red),
        colorRed20 = context.resolveColorAttr(R.attr.color_red_20),
        colorGreen = context.resolveColorAttr(R.attr.color_green),
        colorBlue = context.resolveColorAttr(R.attr.color_blue),
        colorBlue30 = context.resolveColorAttr(R.attr.color_blue_30),
        colorGray = context.resolveColorAttr(R.attr.color_gray),
        colorGrayLight = context.resolveColorAttr(R.attr.color_gray_light),
        colorWhite = context.resolveColorAttr(R.attr.color_white),
        backPrimaryColor = context.resolveColorAttr(R.attr.back_primary),
        backSecondaryColor = context.resolveColorAttr(R.attr.back_secondary),
        backElevatedColor = context.resolveColorAttr(R.attr.back_elevated),
        colorAccent = context.resolveColorAttr(R.attr.color_blue),
        colorPrimary = context.resolveColorAttr(R.attr.color_blue)
    )

    CompositionLocalProvider(LocalAppColors provides appColors, content = content)
}

var LocalAppColors = compositionLocalOf {
    AppColors(
        supportSeparatorColor = Color(0xffe0e0e0),
        supportOverlayColor = Color(0x1f000000),
        labelPrimaryColor = Color(0xff000000),
        labelSecondaryColor = Color(0x8a000000),
        labelTertiaryColor = Color(0x61000000),
        labelDisableColor = Color(0x42000000),
        colorRed = Color(0xffe53935),
        colorRed20 = Color(0x33e53935),
        colorGreen = Color(0xff43a047),
        colorBlue = Color(0xff1e88e5),
        colorBlue30 = Color(0x4d1e88e5),
        colorGray = Color(0xff9e9e9e),
        colorGrayLight = Color(0xffbdbdbd),
        colorWhite = Color(0xffffffff),
        backPrimaryColor = Color(0xffffffff),
        backSecondaryColor = Color(0xfff5f5f5),
        backElevatedColor = Color(0xffffffff),
        colorAccent = Color(0xff1e88e5),
        colorPrimary = Color(0xff1e88e5)
    )
}

@Stable
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

fun Context.resolveColorAttr(attr: Int): Color {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attr, typedValue, true)
    return Color(typedValue.data)
}