package cmm.esmorga.domain.event

import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.Success

interface GetMyEventListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<Success<List<Event>>>
}

class GetMyEventListUseCaseImpl(private val repo: EventRepository) : GetMyEventListUseCase {
    override suspend fun invoke(forceRefresh: Boolean): Result<Success<List<Event>>> {
        return try {
            val result = repo.getMyEvents(forceRefresh)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}