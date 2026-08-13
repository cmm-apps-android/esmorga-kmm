package cmm.esmorga.esmorga.screens.errors

import androidx.compose.runtime.Composable
import cmm.esmorga.designsystem.EsmorgaFullScreenError
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.default_error_button
import cmm.esmorga.shared.generated.resources.default_error_title
import cmm.esmorga.view.theme.EsmorgaTheme
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
