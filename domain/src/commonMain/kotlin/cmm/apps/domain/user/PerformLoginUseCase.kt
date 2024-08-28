package cmm.apps.domain.user

import cmm.apps.domain.result.Success
import cmm.apps.domain.user.model.User
import cmm.apps.domain.user.repository.UserRepository

interface PerformLoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Success<User>>
}

class PerformLoginUseCaseImpl(private val repo: UserRepository) : PerformLoginUseCase {
    override suspend fun invoke(email: String, password: String): Result<Success<User>> {
        try {
            val result = repo.login(email, password)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}