package cmm.esmorga.datasource.local.user

import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.data.user.model.UserDataModel
import cmm.esmorga.datasource.local.database.dao.UserDao
import cmm.esmorga.datasource.local.user.mapper.toUserDataModel
import cmm.esmorga.datasource.local.user.mapper.toUserLocalModel

class UserLocalDatasourceImpl(private val userDao: UserDao) : UserDatasource {
    override suspend fun saveUser(user: UserDataModel) {
        userDao.insertUser(user.toUserLocalModel())
    }

    override suspend fun getUser(): UserDataModel {
        return userDao.getUser().toUserDataModel()
    }
}