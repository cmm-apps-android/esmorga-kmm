package cmm.esmorga.domain.user

import cmm.esmorga.domain.result.Success
import cmm.esmorga.domain.user.repository.UserRepository

interface LogOutUseCase {
    suspend operator fun invoke(): Result<Success<Unit>>
}

class LogOutUseCaseImpl(private val repo: UserRepository) : LogOutUseCase {
    override suspend fun invoke(): Result<Success<Unit>> {
        return try {
            Result.success(repo.logout())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}