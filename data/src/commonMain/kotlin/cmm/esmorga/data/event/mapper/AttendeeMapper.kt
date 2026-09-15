package cmm.esmorga.data.event.mapper

import cmm.esmorga.data.event.model.AttendeeDataModel
import cmm.esmorga.domain.event.model.Attendee

fun AttendeeDataModel.toAttendee() = Attendee(
    name = dataName,
    alreadyPaid = dataAlreadyPaid
)

fun List<AttendeeDataModel>.toAttendeeList() = map { it.toAttendee() }
