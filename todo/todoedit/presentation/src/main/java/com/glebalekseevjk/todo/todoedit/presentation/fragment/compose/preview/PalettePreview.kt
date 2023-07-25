package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.AppTheme


private class ColorMapParameterProvider :
    PreviewParameterProvider<Map<String, @Composable () -> Color>> {
    override val values: Sequence<Map<String, @Composable () -> Color>>
        get() = sequenceOf(
            mapOf(
                "Support [%s] / Separator" to { AppTheme.colors.supportSeparatorColor },
                "Support [%s] / Overlay" to { AppTheme.colors.supportOverlayColor },
            ),
            mapOf(
                "Label [%s] / Primary" to { AppTheme.colors.labelPrimaryColor },
                "Label [%s] / Secondary" to { AppTheme.colors.labelSecondaryColor },
                "Label [%s] / Tertiary" to { AppTheme.colors.labelTertiaryColor },
                "Label [%s] / Disable" to { AppTheme.colors.labelDisableColor },
            ),
            mapOf(
                "Color [%s] / Red" to { AppTheme.colors.colorRed },
                "Color [%s] / Red 20" to { AppTheme.colors.colorRed20 },
                "Color [%s] / Green" to { AppTheme.colors.colorGreen },
                "Color [%s] / Blue" to { AppTheme.colors.colorBlue },
                "Color [%s] / Blue 30" to { AppTheme.colors.colorBlue30 },
                "Color [%s] / Gray" to { AppTheme.colors.colorGray },
                "Color [%s] / Gray Light" to { AppTheme.colors.colorGrayLight },
                "Color [%s] / White" to { AppTheme.colors.colorWhite },
            ),
            mapOf(
                "Back [%s] / Primary" to { AppTheme.colors.backPrimaryColor },
                "Back [%s] / Secondary" to { AppTheme.colors.backSecondaryColor },
                "Back [%s] / Elevated" to { AppTheme.colors.backElevatedColor },
                )
        )

}

@Preview(
    name = "Night",
    uiMode = Configuration.UI_MODE_NIGHT_YES, device = "spec:width=5000px,height=600px,dpi=440"
)
@Preview(
    name = "Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO, device = "spec:width=5000px,height=600px,dpi=440"
)
@Composable
fun PalettePreview(
    @PreviewParameter(ColorMapParameterProvider::class) colorMap: Map<String, @Composable () -> Color>
) {
    ThemePreview {
        Row() {
            for ((name, color) in colorMap) {
                Box(
                    modifier = Modifier
                        .background(color())
                        .width(240.dp)
                        .height(100.dp),
                    contentAlignment = Alignment.BottomStart,
                    content = {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = String.format(name, if(isSystemInDarkTheme()) "Dark" else "Light"),
                                color = if (isColorDark(color.invoke())) Color.White else Color.Black
                            )
                            Text(
                                text = color.invoke().toHex(),
                                color = if (isColorDark(color.invoke())) Color.White else Color.Black
                            )
                        }
                    }
                )
            }
        }
    }
}

fun Color.toHex(): String {
    return "#" +
            Integer.toHexString((alpha * 255).toInt()).padStart(2, '0') +
            Integer.toHexString((red * 255).toInt()).padStart(2, '0') +
            Integer.toHexString((green * 255).toInt()).padStart(2, '0') +
            Integer.toHexString((blue * 255).toInt()).padStart(2, '0')
}

fun isColorDark(color: Color): Boolean {
    val colorInt = color.toArgb()
    val luminance = ColorUtils.calculateLuminance(colorInt)
    return luminance < 0.5
}