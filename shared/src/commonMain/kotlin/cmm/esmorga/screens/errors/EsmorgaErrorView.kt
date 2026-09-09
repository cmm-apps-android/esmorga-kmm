package cmm.esmorga.screens.errors

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.esmorga.designsystem.ErrorScreen
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.default_error_button
import cmm.esmorga.shared.generated.resources.default_error_title
import cmm.esmorga.utils.screenContentInsets
import org.jetbrains.compose.resources.stringResource

@Composable
fun EsmorgaFullScreenError(
    esmorgaErrorScreenArguments: String? = null,
    onButtonPressed: () -> Unit
) {
    val title = esmorgaErrorScreenArguments.takeUnless { it.isNullOrBlank() } ?: stringResource(Res.string.default_error_title)
    Scaffold(
        contentWindowInsets = screenContentInsets()
    ) { innerPadding ->
        ErrorScreen(
            modifier = Modifier.padding(innerPadding).padding(16.dp),
            title = title,
            buttonText = stringResource(Res.string.default_error_button),
            buttonAction = onButtonPressed
        )
    }
}
