package cmm.apps.esmorga.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import cmm.apps.esmorga.view.theme.Claret
import cmm.apps.esmorga.view.theme.DarkClaret
import cmm.apps.esmorga.view.theme.DarkGrey
import cmm.apps.esmorga.view.theme.DarkLavender
import cmm.apps.esmorga.view.theme.DarkPearl
import cmm.apps.esmorga.view.theme.DarkPink
import cmm.apps.esmorga.view.theme.DarkWhiteSmoke
import cmm.apps.esmorga.view.theme.DarkestWhite
import cmm.apps.esmorga.view.theme.Lavender
import cmm.apps.esmorga.view.theme.LightSepia
import cmm.apps.esmorga.view.theme.Pearl
import cmm.apps.esmorga.view.theme.Pink
import cmm.apps.esmorga.view.theme.Sepia
import cmm.apps.esmorga.view.theme.SoftDarkGrey
import cmm.apps.esmorga.view.theme.VeryLightGrey
import cmm.apps.esmorga.view.theme.White
import cmm.apps.esmorga.view.theme.WhiteSmoke


private val LightColorScheme = lightColorScheme(
    primary = Claret,
    onPrimary = VeryLightGrey,
    secondary = Pink,
    onSecondary = DarkGrey,
    background = Lavender,
    surface = Lavender,
    surfaceVariant = Pearl,
    surfaceContainerLow = WhiteSmoke,
    surfaceContainerLowest = White,
    onSurface = DarkGrey,
    onSurfaceVariant = Sepia
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkClaret,
    onPrimary = VeryLightGrey,
    secondary = DarkPink,
    onSecondary = SoftDarkGrey,
    background = DarkLavender,
    surface = DarkLavender,
    surfaceVariant = DarkPearl,
    surfaceContainerLow = DarkWhiteSmoke,
    surfaceContainerLowest = DarkestWhite,
    onSurfaceVariant = LightSepia,
    onSurface = VeryLightGrey
)

@Composable
fun EsmorgaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getEsmorgaTypography(colorScheme),
        content = content
    )
}