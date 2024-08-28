package cmm.apps.esmorga.android.errors

import androidx.compose.runtime.Composable
import cmm.apps.android.designsystem.EsmorgaFullScreenError
import cmm.apps.esmorga.android.theme.EsmorgaTheme

@Composable
fun EsmorgaErrorScreen(
    esmorgaErrorScreenArguments: String,
    onButtonPressed: () -> Unit,
) {
    EsmorgaTheme {
        EsmorgaFullScreenError(
            title = esmorgaErrorScreenArguments,
            buttonText = esmorgaErrorScreenArguments,
            buttonAction = onButtonPressed
        )
    }
}
