package cmm.esmorga.domain.event

import cmm.esmorga.domain.event.model.CreateEventForm
import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.Success

interface CreateEventUseCase {
    suspend operator fun invoke(eventForm: CreateEventForm): Result<Success<Unit>>
}

class CreateEventUseCaseImpl(private val repo: EventRepository) : CreateEventUseCase {
    override suspend fun invoke(eventForm: CreateEventForm): Result<Success<Unit>> {
        return try {
            repo.createEvent(eventForm)
            Result.success(Success(Unit))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
