package cmm.apps.viewmodel.welcome.model

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class WelcomeUiState(
    val primaryButtonText: String = "",
    val secondaryButtonText: String = "",
//    val icon: Int = R.drawable.ic_app
) : KoinComponent {
    fun createDefaultWelcomeUiState(): WelcomeUiState {
        return WelcomeUiState(
            primaryButtonText = "Login",
            secondaryButtonText = "Register",
//            icon = R.drawable.ic_app
        )
    }
}

sealed class WelcomeEffect {
    data object NavigateToEventList : WelcomeEffect()
    data object NavigateToLogin : WelcomeEffect()
}