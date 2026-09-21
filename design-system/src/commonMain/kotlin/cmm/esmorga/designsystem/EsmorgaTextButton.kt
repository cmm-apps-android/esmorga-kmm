package cmm.esmorga.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EsmorgaTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled
    ) {
        EsmorgaText(
            text = text,
            style = EsmorgaTextStyle.BUTTON_SECONDARY,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
