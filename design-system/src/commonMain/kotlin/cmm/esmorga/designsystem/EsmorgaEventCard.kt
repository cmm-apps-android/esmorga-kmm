package cmm.esmorga.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                placeholder = placeholder,
                error = placeholder,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
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
                    Spacer(modifier = Modifier.height(2.dp))
                    EsmorgaText(
                        text = it,
                        style = EsmorgaTextStyle.BODY_1_ACCENT
                    )
                }
            }
        }
    }
}
