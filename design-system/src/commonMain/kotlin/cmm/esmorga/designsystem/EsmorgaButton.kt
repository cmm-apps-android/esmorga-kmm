package cmm.esmorga.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaButton(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    primary: Boolean = true,
    oneLine: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = if (primary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    Button(
        shape = RoundedCornerShape(5.dp),
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = if (primary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
            containerColor = containerColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f)
        ),
        enabled = isEnabled && !isLoading,
        onClick = {
            if (!isLoading) {
                onClick()
            }
        }) {
        if (isLoading) {
            EsmorgaCircularLoader(modifier = Modifier.size(24.dp))
        } else {
            EsmorgaText(
                text = text,
                style = if (primary) EsmorgaTextStyle.BUTTON_PRIMARY else EsmorgaTextStyle.BUTTON_SECONDARY,
                maxLines = if (oneLine) 1 else Int.MAX_VALUE
            )
        }
    }
}
