package cmm.esmorga.data.mock

import cmm.esmorga.data.user.model.UserDataModel


object UserDataMock {

    fun provideUserDataModel(name: String = "Hermione", lastName: String = "Granger", email: String = "hermione@dirtyblood.com"): UserDataModel = UserDataModel(
        dataName = name,
        dataLastName = lastName,
        dataEmail = email
    )
}