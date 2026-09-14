package cmm.esmorga.domain.event

import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.Success

interface GetEventAttendeesUseCase {
    suspend operator fun invoke(eventId: String): Result<Success<List<String>>>
}

class GetEventAttendeesUseCaseImpl(private val repo: EventRepository) : GetEventAttendeesUseCase {
    override suspend fun invoke(eventId: String): Result<Success<List<String>>> {
        return try {
            Result.success(repo.getEventAttendees(eventId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}