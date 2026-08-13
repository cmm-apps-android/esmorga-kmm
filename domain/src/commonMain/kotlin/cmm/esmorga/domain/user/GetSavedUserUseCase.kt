package cmm.esmorga.domain.user

import cmm.esmorga.domain.result.Success
import cmm.esmorga.domain.user.model.User
import cmm.esmorga.domain.user.repository.UserRepository

interface GetSavedUserUseCase {
    suspend operator fun invoke(): Result<Success<User>>
}

class GetSavedUserUseCaseImpl(private val repo: UserRepository) : GetSavedUserUseCase {
    override suspend fun invoke(): Result<Success<User>> {
        try {
            val result = repo.getUser()
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}