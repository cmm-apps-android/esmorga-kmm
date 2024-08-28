package cmm.apps.domain.user

import cmm.apps.domain.result.Success
import cmm.apps.domain.user.model.User
import cmm.apps.domain.user.repository.UserRepository


interface PerformRegistrationUserCase {
    suspend operator fun invoke(name: String, lastName: String, email: String, password: String): Result<Success<User>>
}

class PerformRegistrationUserCaseImpl(private val repo: UserRepository) : PerformRegistrationUserCase {
    override suspend fun invoke(name: String, lastName: String, email: String, password: String): Result<Success<User>> {
        try {
            val result = repo.register(name, lastName, email, password)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}