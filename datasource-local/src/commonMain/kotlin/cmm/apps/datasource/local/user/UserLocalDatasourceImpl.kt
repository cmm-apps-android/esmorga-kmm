package cmm.apps.datasource.local.user

import cmm.apps.data.user.datasource.UserDatasource
import cmm.apps.data.user.model.UserDataModel
import cmm.apps.datasource.local.database.dao.UserDao
import cmm.apps.datasource.local.user.mapper.toUserDataModel
import cmm.apps.datasource.local.user.mapper.toUserLocalModel

class UserLocalDatasourceImpl(private val userDao: UserDao) : UserDatasource {
    override suspend fun saveUser(user: UserDataModel) {
        userDao.insertUser(user.toUserLocalModel())
    }

    override suspend fun getUser(): UserDataModel {
        return userDao.getUser().toUserDataModel()
    }
}