package cmm.apps.viewmodel.eventlist.mapper

import cmm.apps.domain.event.model.Event
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object EventListUiMapper {

    fun formatDate(date: Instant): String {
        val dateTime = date.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = dateTime.dayOfMonth
        val month = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "$day de $month a las $hour:$minute"
    }


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
