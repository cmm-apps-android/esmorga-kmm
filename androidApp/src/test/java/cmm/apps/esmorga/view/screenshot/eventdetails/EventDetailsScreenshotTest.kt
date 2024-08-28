package cmm.apps.esmorga.view.screenshot.eventdetails

import cmm.apps.esmorga.android.eventdetails.EventDetailsView
import cmm.apps.esmorga.android.theme.EsmorgaTheme
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.viewmodel.eventdetails.model.EventDetailsUiState
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
                    onNavigateClicked = {},
                    onBackPressed = {}
                )
            }
        }
    }

}