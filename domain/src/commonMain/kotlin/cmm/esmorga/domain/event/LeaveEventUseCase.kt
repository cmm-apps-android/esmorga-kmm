package cmm.esmorga.domain.event

import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.Success

interface LeaveEventUseCase {
    suspend operator fun invoke(eventId: String): Result<Success<Unit>>
}

class LeaveEventUseCaseImpl(private val repo: EventRepository) : LeaveEventUseCase {
    override suspend fun invoke(eventId: String): Result<Success<Unit>> {
        return try {
            Result.success(repo.leaveEvent(eventId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}