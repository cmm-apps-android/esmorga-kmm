package cmm.esmorga.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cmm.esmorga.design_system.generated.resources.Res
import cmm.esmorga.design_system.generated.resources.cancel_button
import cmm.esmorga.design_system.generated.resources.ok_button
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsmorgaTimePickerDialog(
    state: TimePickerState,
    title: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp,
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EsmorgaText(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    style = EsmorgaTextStyle.BODY_1,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSecondary,
                        clockDialColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectorColor = MaterialTheme.colorScheme.primary,
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    EsmorgaTextButton(
                        text = stringResource(Res.string.cancel_button),
                        onClick = onCancel
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    EsmorgaTextButton(
                        text = stringResource(Res.string.ok_button),
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}
