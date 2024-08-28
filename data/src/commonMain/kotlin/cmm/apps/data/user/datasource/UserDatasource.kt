package cmm.apps.data.user.datasource

import cmm.apps.data.user.model.UserDataModel
import cmm.apps.domain.result.ErrorCodes
import cmm.apps.domain.result.EsmorgaException
import cmm.apps.domain.result.Source

interface UserDatasource {
    suspend fun login(email: String, password: String): Result<UserDataModel> {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
    suspend fun register(name: String, lastName: String, email: String, password: String): UserDataModel {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
    suspend fun saveUser(user: UserDataModel) {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
    suspend fun getUser(): UserDataModel {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
}