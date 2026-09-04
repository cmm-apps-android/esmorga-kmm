package cmm.esmorga.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun EsmorgaEventCard(
    imageUrl: String?,
    title: String,
    subtitle1: String?,
    subtitle2: String?,
    placeholder: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = imageUrl,
            placeholder = placeholder,
            error = placeholder,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
        ) {
            EsmorgaText(
                text = title,
                style = EsmorgaTextStyle.HEADING_2,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            subtitle1?.let {
                EsmorgaText(
                    text = it,
                    style = EsmorgaTextStyle.BODY_1_ACCENT,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            subtitle2?.let {
                EsmorgaText(
                    text = it,
                    style = EsmorgaTextStyle.BODY_1_ACCENT
                )
            }
        }
    }
}
