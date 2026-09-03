package cmm.esmorga.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.esmorga.design_system.generated.resources.Res
import cmm.esmorga.design_system.generated.resources.ic_arrow_forward
import org.jetbrains.compose.resources.painterResource

@Composable
fun EsmorgaRow(
    title: String,
    subtitle: String? = null,
    caption: String? = null,
    modifier: Modifier = Modifier,
    arrowContentDescription: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            EsmorgaText(
                text = title,
                style = EsmorgaTextStyle.HEADING_2,
                maxLines = 1,
            )
            subtitle?.let {
                EsmorgaText(
                    text = subtitle,
                    style = EsmorgaTextStyle.BODY_1,
                    maxLines = 1,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            caption?.let {
                EsmorgaText(
                    text = caption,
                    style = EsmorgaTextStyle.CAPTION,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 74.dp)
                )
            }

            Icon(
                painter = painterResource(Res.drawable.ic_arrow_forward),
                contentDescription = arrowContentDescription
            )
        }
    }
}