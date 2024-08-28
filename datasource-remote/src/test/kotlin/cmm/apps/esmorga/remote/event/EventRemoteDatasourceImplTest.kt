package cmm.apps.esmorga.remote.event

import cmm.apps.datasource.remote.api.EsmorgaApi
import cmm.apps.datasource.remote.event.EventRemoteDatasourceImpl
import cmm.apps.datasource.remote.event.model.EventListWrapperRemoteModel
import cmm.apps.domain.result.ErrorCodes
import cmm.apps.domain.result.EsmorgaException
import cmm.apps.esmorga.remote.mock.EventRemoteMock
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class EventRemoteDatasourceImplTest {

    @Test
    fun `given a working api when events requested then event list is successfully returned`() = runTest {
        val remoteEventName = "RemoteEvent"

        val api = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.getEvents() } returns EventRemoteMock.provideEventListWrapper(listOf(remoteEventName))

        val sut = EventRemoteDatasourceImpl(api)
        val result = sut.getEvents()

        Assert.assertEquals(remoteEventName, result[0].dataName)
    }

    @Test
    fun `given an api returning 500 when events requested then EsmorgaException is thrown`() = runTest {
        val errorCode = 500

        val api = mockk<EsmorgaApi>(relaxed = true)
        val mockResponse = mockk<HttpResponse>(relaxed = true) {
            every { status } returns HttpStatusCode.InternalServerError
        }
        coEvery { api.getEvents() } throws ClientRequestException(
            response = mockResponse,
            cachedResponseText = "Error"
        )

        val sut = EventRemoteDatasourceImpl(api)

        val exception = try {
            sut.getEvents()
            null
        } catch (exception: Throwable) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }

    @Test
    fun `given an api with an event with wrong type when events requested then Exception is thrown`() = runTest {
        val remoteEventName = "RemoteEvent"
        val wrongTypeEvent = EventRemoteMock.provideEvent(remoteEventName).copy(remoteType = "ERROR")

        val api = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.getEvents() } returns EventListWrapperRemoteModel(1, listOf(wrongTypeEvent))

        val sut = EventRemoteDatasourceImpl(api)

        val exception = try {
            sut.getEvents()
            null
        } catch (exception: Throwable) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(ErrorCodes.PARSE_ERROR, (exception as EsmorgaException).code)
    }

    @Test
    fun `given an api with an event with wrong formatted date when events requested then Exception is thrown`() = runTest {
        val remoteEventName = "RemoteEvent"
        val wrongTypeEvent = EventRemoteMock.provideEvent(remoteEventName).copy(remoteDate = "ERROR")

        val api = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.getEvents() } returns EventListWrapperRemoteModel(1, listOf(wrongTypeEvent))

        val sut = EventRemoteDatasourceImpl(api)
        val exception = try {
            sut.getEvents()
            null
        } catch (exception: Throwable) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(ErrorCodes.PARSE_ERROR, (exception as EsmorgaException).code)
    }
}