package cmm.apps.esmorga.view.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font

@Composable
fun getEsmorgaTypography(colorScheme: ColorScheme): Typography {
 /*   val epilogueFontFamily = FontFamily(
        Font(Res.font.epilogue_flex, FontWeight.Bold),
        Font(Res.font.epilogue_flex, FontWeight.Medium),
        Font(Res.font.epilogue_flex, FontWeight.Normal)
    )

    val jackartaFontFamily = FontFamily(
        Font(Res.font.plus_jakarta_flex, FontWeight.Bold),
        Font(Res.font.plus_jakarta_flex, FontWeight.Medium),
        Font(Res.font.plus_jakarta_flex, FontWeight.Normal)
    )
*/
    return Typography(
        titleLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.33).sp,
            color = colorScheme.onSurface
        ),
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 27.5.sp,
            letterSpacing = (-0.33).sp,
            color = colorScheme.onSurface
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 22.5.sp,
            letterSpacing = (-0.27).sp,
            color = colorScheme.onSurface
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = colorScheme.onSurface
        ),
        labelLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            letterSpacing = 0.20.sp,
            color = colorScheme.onSurface
        ),
        labelSmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = colorScheme.onSurfaceVariant
        )
    )
}
