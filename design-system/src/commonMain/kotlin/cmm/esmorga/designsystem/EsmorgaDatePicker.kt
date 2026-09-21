package cmm.esmorga.designsystem

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsmorgaDatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier
) {
    DatePicker(
        modifier = modifier,
        state = state,
        showModeToggle = false,
        title = null,
        headline = null,
        colors = DatePickerDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.background),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
class PossibleSelectableDates(private val startOfToday: Long) : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= startOfToday
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class DeadlineSelectableDates(private val startOfToday: Long, private val eventDateMidnightMillis: Long) : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis in startOfToday..eventDateMidnightMillis
    }
}