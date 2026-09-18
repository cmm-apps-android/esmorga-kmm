package cmm.esmorga.viewmodel.explore.mapper

import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.viewmodel.common.formatDayOfWeekMediumDateShortTime
import cmm.esmorga.viewmodel.explore.model.EventListUiModel
import kotlin.time.Instant

object EventListUiMapper {

    fun formatDate(date: Instant): String = formatDayOfWeekMediumDateShortTime(date)


    private fun Event.toEventUi(): EventListUiModel {

        return EventListUiModel(
            id = this.id,
            imageUrl = this.imageUrl,
            cardTitle = this.name,
            cardSubtitle1 = formatDate(date),
            cardSubtitle2 = this.location.name
        )
    }

    fun List<Event>.toEventUiList() = this.map { ev -> ev.toEventUi() }

}
