package cmm.apps.esmorga.android.errors.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class EsmorgaErrorScreenArguments(
    val title: String,
    val buttonText: String,
) : Parcelable