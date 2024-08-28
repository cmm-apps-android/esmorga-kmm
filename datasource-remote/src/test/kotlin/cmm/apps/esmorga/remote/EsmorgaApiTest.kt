package cmm.apps.esmorga.remote

import cmm.apps.datasource.remote.api.EsmorgaApi
import cmm.apps.esmorga.remote.mock.MockServer.getContentFromJSONFile
import cmm.apps.esmorga.remote.mock.MockServer.provideTestHttpClient
import cmm.apps.esmorga.remote.mock.json.ServerFiles
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EsmorgaApiTest {

    @Test
    fun `given a successful mock server when events are requested then a correct eventWrapper is returned`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = getContentFromJSONFile(ServerFiles.GET_EVENTS),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }


        val sut = EsmorgaApi(provideTestHttpClient(mockEngine))

        val eventWrapper = sut.getEvents()

        Assert.assertEquals(2, eventWrapper.remoteEventList.size)
        Assert.assertTrue(eventWrapper.remoteEventList[0].remoteName.contains("MobgenFest"))
    }

    @Test
    fun `given a successful mock server when login is requested then a correct user is returned`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = getContentFromJSONFile(ServerFiles.LOGIN),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type" to listOf("application/json"))
            )
        }

        val sut = EsmorgaApi(provideTestHttpClient(mockEngine))

        val user = sut.login(body = mapOf("email" to "email", "password" to "password"))

        Assert.assertEquals("Albus", user.remoteProfile.remoteName)
    }
}