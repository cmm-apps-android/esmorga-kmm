package cmm.esmorga.view.screenshot.eventdetails

import cmm.esmorga.screens.eventdetails.EventDetailsView
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.view.screenshot.BaseScreenshotTest
import cmm.esmorga.viewmodel.eventdetails.model.EventDetailsUiState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import org.junit.Test

class EventDetailsScreenshotTest : BaseScreenshotTest() {

    @Test
    fun eventDetailsView_lightTheme_no_location() {
        snapshotWithState(lat = null, lng = null)
    }

    @Test
    fun eventDetailsView_lightTheme_data() {
        snapshotWithState()
    }

    private fun snapshotWithState(lat: Double? = 0.0, lng: Double? = 2.88) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventDetailsView(
                    uiState = EventDetailsUiState(
                        id = "1",
                        title = "Mobgen fest",
                        subtitle = "35 de Mayo a las 27:00",
                        description = "El mejor evento del año",
                        image = "test.png",
                        locationName = "Mi casa",
                        locationLat = lat,
                        locationLng = lng
                    ),
                    snackbarHostState = remember { SnackbarHostState() },
                    onNavigateClicked = {},
                    onViewAttendeesClicked = {},
                    onJoinLeaveClicked = {},
                    onBackPressed = {}
                )
            }
        }
    }

}