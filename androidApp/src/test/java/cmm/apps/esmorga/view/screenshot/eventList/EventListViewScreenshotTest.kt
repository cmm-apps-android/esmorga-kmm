package cmm.apps.esmorga.view.screenshot.eventList

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.android.eventlist.EventListView
import cmm.apps.esmorga.android.theme.EsmorgaTheme
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.EventListUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import org.junit.Test

class EventListViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun eventListView_lightTheme_empty() {
        snapshotWithState(loading = false, eventList = listOf(), error = null)
    }

    @Test
    fun eventListView_lightTheme_error() {
        snapshotWithState(loading = false, eventList = listOf(), error = "Error")
    }

    @Test
    fun eventListView_lightTheme_loading() {
        snapshotWithState(loading = true, eventList = listOf(), error = null)
    }

    @Test
    fun eventListView_lightTheme_data() {
        val event = EventListUiModel(
            id = "1",
            imageUrl = "test.png",
            cardTitle = "Card Title",
            cardSubtitle1 = "Card subtitle 1",
            cardSubtitle2 = "Card subtitle 2",
        )

        snapshotWithState(loading = false, eventList = listOf(event, event), error = null)
    }

    private fun snapshotWithState(loading: Boolean, eventList: List<EventListUiModel>, error: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventListView(
                    uiState = EventListUiState(loading = loading, eventList = eventList, error = error),
                    snackbarHostState = SnackbarHostState(),
                    onRetryClick = { },
                    onEventClick = { }
                )
            }
        }
    }

}