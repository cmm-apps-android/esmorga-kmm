package cmm.esmorga.domain.user

import cmm.esmorga.domain.result.Success
import cmm.esmorga.domain.user.PerformLoginUseCaseImpl
import cmm.esmorga.domain.user.repository.UserRepository
import cmm.esmorga.domain.mock.UserDomainMock
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PerformLoginUseCaseImplTest {

    @Test
    fun `given a successful repository when login is performed then user is returned`() = runTest {
        val repoUserName = "Luna"
        val repoUser = UserDomainMock.provideUser(name = repoUserName)
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.login(any(), any()) } returns Success(repoUser)

        val sut = PerformLoginUseCaseImpl(repo)
        val result = sut.invoke(repoUser.email, "password")
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(repoUserName, result.getOrThrow().data.name)
    }
}