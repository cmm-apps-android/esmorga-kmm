package cmm.esmorga.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaCheckbox(
    checked: Boolean,
    onCheckedChanged: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChanged,
        modifier = modifier
    )
}

@Composable
fun EsmorgaCheckboxRow(
    text: String,
    shouldShowChecked: Boolean,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val rowModifier = if (shouldShowChecked) {
        modifier.toggleable(
            value = checked,
            role = Role.Checkbox,
            onValueChange = onCheckedChanged
        )
    } else {
        modifier
    }

    Column(modifier = rowModifier) {
        Row(
            modifier = Modifier.padding(all = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EsmorgaText(
                text = text,
                style = EsmorgaTextStyle.CAPTION,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (shouldShowChecked) {
                Spacer(modifier = Modifier.weight(1f))
                EsmorgaCheckbox(
                    checked = checked,
                    onCheckedChanged = null
                )
            }
        }

        EsmorgaHorizontalDivider()
    }
}
