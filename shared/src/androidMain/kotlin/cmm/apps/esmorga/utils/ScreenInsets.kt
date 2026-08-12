package cmm.apps.esmorga.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable

@Composable
actual fun screenContentInsets(): WindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical)

@Composable
actual fun screenTopBarInsets(): WindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
