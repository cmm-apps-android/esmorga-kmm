package cmm.esmorga.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import cmm.esmorga.design_system.generated.resources.Res
import cmm.esmorga.design_system.generated.resources.ic_error
import org.jetbrains.compose.resources.painterResource


@Composable
fun EsmorgaFullScreenError(
    title: String,
    subtitle: String? = null,
    buttonText: String,
    buttonAction: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_error),
                contentDescription = "Error",
                modifier = Modifier.size(128.dp),
                colorFilter = ColorFilter.tint(colorScheme.primary)
            )
            EsmorgaText(text = title, style = EsmorgaTextStyle.HEADING_1)
            Spacer(modifier = Modifier.size(8.dp))
            subtitle?.let {
                EsmorgaText(text = subtitle, style = EsmorgaTextStyle.BODY_1)
            }
        }
        EsmorgaButton(
            text = buttonText,
            onClick = buttonAction,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}
