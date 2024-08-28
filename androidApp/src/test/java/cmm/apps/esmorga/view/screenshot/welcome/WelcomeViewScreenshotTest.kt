package cmm.apps.esmorga.view.screenshot.welcome

import cmm.apps.esmorga.android.theme.EsmorgaTheme
import cmm.apps.esmorga.android.welcome.WelcomeView
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.viewmodel.welcome.model.WelcomeUiState
import org.junit.Test

class WelcomeViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun welcomeView_lightTheme_data() {
        val uiState = WelcomeUiState(
            primaryButtonText = "Primary",
            secondaryButtonText = "Secondary",
        )

        snapshotWithState(uiState)
    }

    private fun snapshotWithState(uiState: WelcomeUiState) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                WelcomeView(uiState = uiState, onPrimaryButtonClicked = { }) {

                }
            }
        }
    }
}