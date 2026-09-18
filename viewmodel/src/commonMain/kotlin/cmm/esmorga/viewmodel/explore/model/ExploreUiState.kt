package cmm.esmorga.viewmodel.explore.model
data class ExploreUiState(
    val isLoading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: Boolean = false
)

data class EventListUiModel(
    val id: String,
    val imageUrl: String?,
    val cardTitle: String,
    val cardSubtitle1: String,
    val cardSubtitle2: String
)

sealed class EventListEffect {
    data object ShowNoNetworkPrompt : EventListEffect()
    data class NavigateToEventDetail(val eventId: String) : EventListEffect()
}
