package cmm.apps.esmorga.android.errors

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import cmm.apps.designsystem.EsmorgaFullScreenError
import cmm.apps.esmorga.android.theme.EsmorgaTheme

@Composable
fun EsmorgaErrorScreen(
    esmorgaErrorScreenArguments: String,
    icon: Painter,
    onButtonPressed: () -> Unit,
) {
    EsmorgaTheme {
        EsmorgaFullScreenError(
            title = esmorgaErrorScreenArguments,
            buttonText = esmorgaErrorScreenArguments,
            icon = icon,
            buttonAction = onButtonPressed
        )
    }
}
