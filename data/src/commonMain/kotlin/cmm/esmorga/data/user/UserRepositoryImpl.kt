package cmm.esmorga.data.user

import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.data.user.mapper.toUser
import cmm.esmorga.domain.result.Success
import cmm.esmorga.domain.user.model.User
import cmm.esmorga.domain.user.repository.UserRepository

class UserRepositoryImpl(private val localDs: UserDatasource, private val remoteDs: UserDatasource): UserRepository {
    override suspend fun login(email: String, password: String): Success<User> {
        try {
            val userResult = remoteDs.login(email, password)
            val userDataModel = userResult.getOrThrow()
            localDs.saveUser(userDataModel)
            return Success(userDataModel.toUser())
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun register(name: String, lastName: String, email: String, password: String): Success<User> {
        val userDataModel = remoteDs.register(name, lastName, email, password)
        localDs.saveUser(userDataModel)
        return Success(userDataModel.toUser())
    }

    override suspend fun getUser(): Success<User> {
        try {
            val userDataModel = localDs.getUser()
            return Success(userDataModel.toUser())
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun logout(): Success<Unit> {
        localDs.logout()
        return Success(Unit)
    }
}