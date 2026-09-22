package cmm.esmorga.viewmodel.explore.mapper

import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.viewmodel.common.DateUtils
import cmm.esmorga.viewmodel.explore.model.EventListUiModel

object EventListUiMapper {


    private fun Event.toEventUi(): EventListUiModel {

        return EventListUiModel(
            id = this.id,
            imageUrl = this.imageUrl,
            cardTitle = this.name,
            cardSubtitle1 = DateUtils.formatDayOfWeekMediumDateShortTime(date),
            cardSubtitle2 = this.location.name
        )
    }

    fun List<Event>.toEventUiList() = this.map { ev -> ev.toEventUi() }

}
