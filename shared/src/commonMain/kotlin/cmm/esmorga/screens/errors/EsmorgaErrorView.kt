package cmm.esmorga.screens.errors

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cmm.esmorga.designsystem.EsmorgaFullScreenError
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.default_error_button
import cmm.esmorga.shared.generated.resources.default_error_title
import cmm.esmorga.utils.screenContentInsets
import org.jetbrains.compose.resources.stringResource

@Composable
fun EsmorgaErrorScreen(
    esmorgaErrorScreenArguments: String,
    onButtonPressed: () -> Unit
) {
    val title = esmorgaErrorScreenArguments.ifBlank { stringResource(Res.string.default_error_title) }
    Scaffold(
        contentWindowInsets = screenContentInsets()
    ) { innerPadding ->
        EsmorgaFullScreenError(
            modifier = Modifier.padding(innerPadding),
            title = title,
            buttonText = stringResource(Res.string.default_error_button),
            buttonAction = onButtonPressed
        )
    }
}
