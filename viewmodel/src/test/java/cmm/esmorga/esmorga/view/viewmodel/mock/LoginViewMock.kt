package cmm.esmorga.view.viewmodel.mock

import cmm.esmorga.domain.user.model.User

object LoginViewMock {

    fun provideUser(name: String = "Minerva", lastname: String = "McGonagall", email: String = "mi_mcgonagall@hogwarts.edu"): User = User(name, lastname, email)
}