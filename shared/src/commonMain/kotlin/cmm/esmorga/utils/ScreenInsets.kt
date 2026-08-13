package cmm.esmorga.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable

@Composable
expect fun screenContentInsets(): WindowInsets

@Composable
expect fun screenTopBarInsets(): WindowInsets
