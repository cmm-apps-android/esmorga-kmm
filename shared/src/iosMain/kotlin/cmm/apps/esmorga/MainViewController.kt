package cmm.apps.esmorga

import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import cmm.apps.esmorga.di.sharedKoinModules
import org.koin.core.context.startKoin

private var isKoinStarted = false

fun MainViewController() = run {
    if (!isKoinStarted) {
        startKoin {
            modules(ViewDIModule.modules + sharedKoinModules)
        }
        isKoinStarted = true
    }
    ComposeUIViewController(
        configure = {
            onFocusBehavior = OnFocusBehavior.DoNothing
        }
    ) { App() }
}