package cmm.esmorga.navigation

import kotlinx.serialization.Serializable

sealed class Navigation {

    @Serializable
    data object WelcomeScreen : Navigation()

    @Serializable
    data object EventListScreen : Navigation()

    @Serializable
    data class EventDetailScreen(val eventId: String) : Navigation()

    @Serializable
    data object LoginScreen : Navigation()

    @Serializable
    data object RegistrationScreen : Navigation()

    @Serializable
    data class FullScreenError(val esmorgaErrorScreenArguments: String) : Navigation()
}
