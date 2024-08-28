package cmm.apps.esmorga.datasource_local.event

import cmm.apps.datasource.local.database.dao.EventDao
import cmm.apps.datasource.local.event.EventLocalDatasourceImpl
import cmm.apps.datasource.local.event.mapper.toEventDataModelList
import cmm.apps.datasource.local.event.model.EventLocalModel
import cmm.apps.esmorga.datasource_local.mock.EventLocalMock
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test

class EventLocalDatasourceImplTest {

    private val fakeStorage = mutableListOf<String>()

    private fun provideFakeDao(): EventDao {
        val eventListSlot = slot<List<EventLocalModel>>()
        val singleEventSlot = slot<String>()
        val dao = mockk<EventDao>()
        coEvery { dao.getEvents() } coAnswers {
            fakeStorage.map { name ->
                EventLocalMock.provideEvent(name)
            }
        }
        coEvery { dao.insertEvent(capture(eventListSlot)) } coAnswers {
            fakeStorage.addAll(eventListSlot.captured.map { event -> event.localName })
        }
        coEvery { dao.deleteAll() } coAnswers {
            fakeStorage.clear()
        }

        coEvery { dao.getEventById(capture(singleEventSlot)) } coAnswers {
            EventLocalMock.provideEvent(fakeStorage.find { it == singleEventSlot.captured }!!)
        }

        return dao
    }

    @After
    fun shutDown() {
        fakeStorage.clear()
    }

    @Test
    fun `given a working dao when events requested then events successfully returned`() = runTest {
        val localEventName = "LocalEvent"

        val dao = mockk<EventDao>(relaxed = true)
        coEvery { dao.getEvents() } returns EventLocalMock.provideEventList(listOf(localEventName))

        val sut = EventLocalDatasourceImpl(dao)
        val result = sut.getEvents()

        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given an empty storage when events cached then events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"

        val sut = EventLocalDatasourceImpl(provideFakeDao())
        sut.cacheEvents(EventLocalMock.provideEventList(listOf(localEventName)).toEventDataModelList())
        val result = sut.getEvents()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given a storage with events when events cached then old events are removed and new events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"
        fakeStorage.add("ShouldBeRemoved")

        val sut = EventLocalDatasourceImpl(provideFakeDao())
        sut.cacheEvents(EventLocalMock.provideEventList(listOf(localEventName)).toEventDataModelList())
        val result = sut.getEvents()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given a storage with events when single event is requested then is returned successfully`() = runTest {
        val localEventName = "LocalEvent"
        fakeStorage.add(localEventName)

        val sut = EventLocalDatasourceImpl(provideFakeDao())
        val result = sut.getEventById(localEventName)

        Assert.assertEquals(localEventName, result.dataName)
    }

}