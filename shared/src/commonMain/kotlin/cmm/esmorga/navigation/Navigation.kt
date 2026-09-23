package cmm.esmorga.navigation

import kotlinx.serialization.Serializable

sealed class Navigation {

    @Serializable
    data object HomeScreen : Navigation()

    @Serializable
    data object EventListScreen : Navigation()

    @Serializable
    data class EventDetailScreen(val eventId: String) : Navigation()

    @Serializable
    data class EventAttendeesScreen(val eventId: String) : Navigation()

    @Serializable
    data object LoginScreen : Navigation()

    @Serializable
    data object RegistrationScreen : Navigation()

    @Serializable
    data object ChangePasswordScreen : Navigation()

    @Serializable
    data class FullScreenError(val esmorgaErrorScreenArguments: String? = null) : Navigation()

    @Serializable
    data object CreateEventFlow : Navigation()

    @Serializable
    data object CreateEventStep1 : Navigation()

    @Serializable
    data object CreateEventStep2 : Navigation()

    @Serializable
    data object CreateEventStep3 : Navigation()

    @Serializable
    data object CreateEventStep4 : Navigation()

    @Serializable
    data object CreateEventStep5 : Navigation()
}

object NavigationKeys {
    const val PASSWORD_CHANGE_SUCCESS = "password_change_success"
}