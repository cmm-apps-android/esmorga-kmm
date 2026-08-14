package cmm.esmorga.domain.event

import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.Success

interface GetEventDetailsUseCase {
    suspend operator fun invoke(id: String): Result<Success<Event>>
}

class GetEventDetailsUseCaseImpl(private val repo: EventRepository) : GetEventDetailsUseCase {
    override suspend fun invoke(id: String): Result<Success<Event>> {
        try {
            return Result.success(repo.getEventDetails(id))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}