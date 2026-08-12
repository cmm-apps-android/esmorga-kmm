package cmm.apps.esmorga.screens.errors

import androidx.compose.runtime.Composable
import cmm.apps.designsystem.EsmorgaFullScreenError
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import esmorga.shared.generated.resources.Res
import esmorga.shared.generated.resources.default_error_button
import esmorga.shared.generated.resources.default_error_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun EsmorgaErrorScreen(
    esmorgaErrorScreenArguments: String,
    onButtonPressed: () -> Unit
) {
    val title = esmorgaErrorScreenArguments.ifBlank { stringResource(Res.string.default_error_title) }
    EsmorgaTheme {
        EsmorgaFullScreenError(
            title = title,
            buttonText = stringResource(Res.string.default_error_button),
            buttonAction = onButtonPressed
        )
    }
}
