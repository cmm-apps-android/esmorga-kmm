package cmm.esmorga.domain.event

import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.Success

interface JoinEventUseCase {
    suspend operator fun invoke(eventId: String): Result<Success<Unit>>
}

class JoinEventUseCaseImpl(private val repo: EventRepository) : JoinEventUseCase {
    override suspend fun invoke(eventId: String): Result<Success<Unit>> {
        return try {
            Result.success(repo.joinEvent(eventId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

