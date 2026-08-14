package cmm.esmorga.datasource_local.mock

import cmm.esmorga.datasource.local.user.model.UserLocalModel

object UserLocalMock {

    fun provideUser(email: String = "severus-snape@hogwarts.edu", name: String = "Severus", lastName: String = "Snape") = UserLocalModel(
        localEmail = email,
        localName = name,
        localLastName = lastName
    )
}