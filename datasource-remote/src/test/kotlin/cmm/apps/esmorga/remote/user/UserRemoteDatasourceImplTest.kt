package cmm.apps.esmorga.remote.user

import cmm.apps.datasource.remote.api.EsmorgaApi
import cmm.apps.datasource.remote.user.UserRemoteDatasourceImpl
import cmm.apps.esmorga.remote.mock.UserRemoteMock
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UserRemoteDatasourceImplTest {

    @Test
    fun `given valid credentials when login succeed then user is returned`() = runTest {
        val remoteUserName = "Albus"

        val api = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.login(any()) } returns UserRemoteMock.provideUser(remoteUserName)

        val sut = UserRemoteDatasourceImpl(api)
        val result = sut.login("email", "password")

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(remoteUserName, result.getOrThrow().dataName)
    }

    @Test(expected = Exception::class)
    fun `given invalid credentials when login fails then exception is thrown`() = runTest {
        val api = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.getEvents() } throws ClientRequestException(
            response = mockk {
                every { status } returns HttpStatusCode.Unauthorized
            },
            cachedResponseText = "Error"
        )

        val sut = UserRemoteDatasourceImpl(api)
        sut.login("email", "password")
    }

}