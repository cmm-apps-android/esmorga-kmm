package cmm.apps.data.user

import cmm.apps.data.user.datasource.UserDatasource
import cmm.apps.data.user.mapper.toUser
import cmm.apps.domain.result.Success
import cmm.apps.domain.user.model.User
import cmm.apps.domain.user.repository.UserRepository

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
}