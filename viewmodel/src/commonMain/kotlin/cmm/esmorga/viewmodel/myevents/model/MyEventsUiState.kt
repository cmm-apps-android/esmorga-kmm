package cmm.esmorga.viewmodel.myevents.model

import cmm.esmorga.viewmodel.explore.model.EventListUiModel


data class MyEventsUiState(
    val isLoggedIn: Boolean = false,
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val isAdmin: Boolean = false,
    val error: Boolean = false
) {
    val createEventEnabled = isAdmin && !loading && !error
}

sealed class MyEventsEffect {
    data object NavigateToLogin : MyEventsEffect()
    data class NavigateToEventDetail(val eventId: String) : MyEventsEffect()
    data object NavigateToCreateEvent : MyEventsEffect()
}
