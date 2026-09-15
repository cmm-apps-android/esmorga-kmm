package cmm.esmorga.domain.event

import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.Success

interface SaveAttendeePaymentUseCase {
    suspend operator fun invoke(eventId: String, userName: String, paid: Boolean): Result<Success<Unit>>
}

class SaveAttendeePaymentUseCaseImpl(private val repo: EventRepository) : SaveAttendeePaymentUseCase {
    override suspend fun invoke(eventId: String, userName: String, paid: Boolean): Result<Success<Unit>> {
        return try {
            Result.success(repo.saveAttendeePayment(eventId, userName, paid))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
