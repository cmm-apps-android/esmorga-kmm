package cmm.esmorga.datasource.remote.user

import cmm.esmorga.datasource.remote.user.mapper.toUserDataModel
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Source
import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.data.user.model.UserDataModel
import cmm.esmorga.datasource.remote.api.EsmorgaApi
import cmm.esmorga.datasource.remote.api.ExceptionHandler

class UserRemoteDatasourceImpl(private val api: EsmorgaApi) : UserDatasource {
    override suspend fun login(email: String, password: String): Result<UserDataModel> {
        try {
            val loginBody = mapOf("email" to email, "password" to password)
            val user = api.login(loginBody)
            return Result.success(user.toUserDataModel())
        } catch (e: Throwable) {
            throw ExceptionHandler.manageApiException(e)
        }
    }

    override suspend fun register(name: String, lastName: String, email: String, password: String): UserDataModel {
        try {
            val registerBody = mapOf(
                "name" to name,
                "lastName" to lastName,
                "email" to email,
                "password" to password
            )
            val user = api.register(registerBody)
            return user.toUserDataModel()
        } catch (e: Exception) {
            throw ExceptionHandler.manageApiException(e)
        }
    }
}