package cmm.esmorga.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * A custom dialog for the Esmorga design system.
 *
 * @param title Optional title text to display.
 * @param description Optional description text to display below the title.
 * @param confirmButtonText The text for the confirmation button.
 * @param onConfirm The action to perform when the confirmation button is clicked.
 * @param onDismiss The action to perform when the dialog is dismissed or the dismiss button is clicked.
 * @param modifier The modifier to be applied to the dialog's surface.
 * @param dismissButtonText Optional text for the dismiss button. If null, the button won't be shown.
 */
@Composable
fun EsmorgaDialog(
    title: String? = null,
    description: String? = null,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButtonText: String? = null,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let {
                    EsmorgaText(
                        text = it,
                        style = EsmorgaTextStyle.HEADING_1,
                        modifier = Modifier.padding(bottom = if (description != null) 8.dp else 16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                description?.let {
                    EsmorgaText(
                        text = it,
                        style = EsmorgaTextStyle.BODY_1,
                        modifier = Modifier.padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    dismissButtonText?.let {
                        EsmorgaButton(
                            text = it,
                            onClick = onDismiss,
                            oneLine = true,
                            modifier = Modifier.weight(1f),
                            primary = false
                        )
                    }
                    EsmorgaButton(
                        text = confirmButtonText,
                        onClick = onConfirm,
                        oneLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
