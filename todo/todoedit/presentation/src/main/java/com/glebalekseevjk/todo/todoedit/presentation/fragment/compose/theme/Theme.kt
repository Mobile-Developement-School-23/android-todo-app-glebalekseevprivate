package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.glebalekseevjk.auth.domain.entity.NightMode

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
    nightMode: NightMode = NightMode.SYSTEM,
    content: @Composable () -> Unit
) {

    val appColors = when (nightMode) {
        NightMode.NIGHT -> nightAppColors
        NightMode.DAY -> dayAppColors
        NightMode.SYSTEM -> if (isSystemInDarkTheme()) nightAppColors else dayAppColors
    }

    CompositionLocalProvider(LocalAppColors provides appColors, content = content)
}

val dayAppColors = AppColors(
    supportSeparatorColor = Color(0x33000000),
    supportOverlayColor = Color(0x0F000000),
    labelPrimaryColor = Color(0xFF000000),
    labelSecondaryColor = Color(0x99000000),
    labelTertiaryColor = Color(0x4D000000),
    labelDisableColor = Color(0x26000000),
    colorRed = Color(0xFFFF3B30),
    colorRed20 = Color(0x33FF3B30),
    colorGreen = Color(0xFF34C759),
    colorBlue = Color(0xFF007AFF),
    colorBlue30 = Color(0x50007AFF),
    colorGray = Color(0xFF8E8E93),
    colorGrayLight = Color(0xFFD1D1D6),
    colorWhite = Color(0xFFFFFFFF),
    backPrimaryColor = Color(0xFFF7F6F2),
    backSecondaryColor = Color(0xFFFFFFFF),
    backElevatedColor = Color(0xFFFFFFFF),
    colorAccent = Color(0xFF007AFF),
    colorPrimary = Color(0xFF007AFF)
)

val nightAppColors = AppColors(
    supportSeparatorColor = Color(0x33FFFFFF),
    supportOverlayColor = Color(0x52000000),
    labelPrimaryColor = Color(0xFFFFFFFF),
    labelSecondaryColor = Color(0x99FFFFFF),
    labelTertiaryColor = Color(0x66FFFFFF),
    labelDisableColor = Color(0x26FFFFFF),
    colorRed = Color(0xFFFF453A),
    colorRed20 = Color(0x33FF453A),
    colorGreen = Color(0xFF32D74B),
    colorBlue = Color(0xFF0A84FF),
    colorBlue30 = Color(0x500A84FF),
    colorGray = Color(0xFF8E8E93),
    colorGrayLight = Color(0xFF48484A),
    colorWhite = Color(0xFFFFFFFF),
    backPrimaryColor = Color(0xFF161618),
    backSecondaryColor = Color(0xFF252528),
    backElevatedColor = Color(0xFF3C3C3F),
    colorAccent = Color(0xFF0A84FF),
    colorPrimary = Color(0xFF0A84FF)
)

var LocalAppColors = compositionLocalOf {
    dayAppColors
}

@Stable
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}