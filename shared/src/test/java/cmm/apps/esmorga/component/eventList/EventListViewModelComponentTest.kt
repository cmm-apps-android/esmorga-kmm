package cmm.apps.esmorga.component.eventList

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import cmm.apps.data.di.DataDIModule
import cmm.apps.data.di.DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME
import cmm.apps.data.event.datasource.EventDatasource
import cmm.apps.datasource.local.database.EsmorgaDatabase
import cmm.apps.datasource.remote.api.EsmorgaApi
import cmm.apps.datasource.remote.event.EventRemoteDatasourceImpl
import cmm.apps.domain.event.GetEventListUseCase
import cmm.apps.esmorga.component.mock.EventDataMock
import cmm.apps.esmorga.component.mock.MockServer.getContentFromJSONFile
import cmm.apps.esmorga.component.mock.MockServer.provideTestHttpClient
import cmm.apps.esmorga.di.sharedKoinModules
import cmm.apps.viewmodel.eventlist.EventListViewModel
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class EventListViewModelComponentTest : KoinTest {

    private lateinit var mockContext: Context
    private lateinit var mockDatabase: EsmorgaDatabase
    private lateinit var mockEngine: MockEngine
    private lateinit var remoteDatasource: EventDatasource

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() {
        Dispatchers.setMain(testDispatcher)
        mockEngine = MockEngine {
            respond(
                content = getContentFromJSONFile("getEvents.json"),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        mockContext = ApplicationProvider.getApplicationContext()
        mockDatabase = Room
            .inMemoryDatabaseBuilder(mockContext, EsmorgaDatabase::class.java)
            .allowMainThreadQueries()
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .build()
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        mockDatabase.close()

        stopKoin()
    }

    private fun startDI() {
        startKoin {
            androidContext(mockContext)
            modules(
                sharedKoinModules + module {
                    single<EsmorgaDatabase> { mockDatabase }
                    factory<EventDatasource>(named(REMOTE_DATASOURCE_INSTANCE_NAME)) { remoteDatasource }
                }
            )
        }
    }

    @Test
    fun `given a successful API and an empty DB when screen is shown then UI state with events is returned`() = runTest {
        val remoteEventName = "RemoteEvent"
        remoteDatasource = mockk<EventDatasource>()
        coEvery { remoteDatasource.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(remoteEventName))
        startDI()

        val useCase: GetEventListUseCase by inject()

        val sut = EventListViewModel(useCase)

        sut.loadEvents()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.eventList[0].cardTitle.contains(remoteEventName))
    }
}
