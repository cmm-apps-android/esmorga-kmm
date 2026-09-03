package cmm.esmorga.viewmodel.myevents.model

import cmm.esmorga.viewmodel.eventlist.model.EventListUiModel


data class MyEventsUiState(
    val isLoggedIn: Boolean = false,
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: String? = null
)

sealed class MyEventsEffect {
    data object NavigateToLogin : MyEventsEffect()
    data class NavigateToEventDetail(val eventId: String) : MyEventsEffect()
}
