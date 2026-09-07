package cmm.esmorga.domain.user

import cmm.esmorga.domain.result.Success
import cmm.esmorga.domain.user.repository.UserRepository

interface ChangePasswordUseCase {
    suspend operator fun invoke(currentPassword: String, newPassword: String): Result<Success<Unit>>
}

class ChangePasswordUseCaseImpl(private val repo: UserRepository) : ChangePasswordUseCase {
    override suspend fun invoke(currentPassword: String, newPassword: String): Result<Success<Unit>> {
        return try {
            Result.success(repo.changePassword(currentPassword = currentPassword, newPassword = newPassword))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}