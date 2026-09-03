package cmm.esmorga.datasource_local.user

import cmm.esmorga.datasource.local.database.dao.EventDao
import cmm.esmorga.datasource.local.database.dao.UserDao
import cmm.esmorga.datasource.local.user.UserLocalDatasourceImpl
import cmm.esmorga.datasource.local.user.mapper.toUserDataModel
import cmm.esmorga.datasource.local.user.model.UserLocalModel
import cmm.esmorga.datasource_local.mock.UserLocalMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test

class UserLocalDatasourceImplTest {
    private var fakeStorage: UserLocalModel? = null
    private var eventDataCleared: Boolean = false

    private fun provideFakeDao(): UserDao {
        val userSlot = slot<UserLocalModel>()
        val dao = mockk<UserDao>()
        coEvery { dao.getUser() } coAnswers {
            fakeStorage ?: throw Exception("User not found")
        }
        coEvery { dao.insertUser(capture(userSlot)) } coAnswers {
            fakeStorage = userSlot.captured
        }
        coEvery { dao.deleteUser() } coAnswers {
            fakeStorage = null
        }

        return dao
    }

    private fun provideFakeEventDao(): EventDao {
        val dao = mockk<EventDao>(relaxed = true)
        coEvery { dao.deleteAll() } coAnswers {
            eventDataCleared = true
        }
        return dao
    }

    @After
    fun shutDown() {
        fakeStorage = null
        eventDataCleared = false
    }

    @Test
    fun `given a working dao when user requested then user successfully returned`() = runTest {
        val localUserName = "Draco"

        val dao = mockk<UserDao>(relaxed = true)
        coEvery { dao.getUser() } returns UserLocalMock.provideUser(name = localUserName)

        val sut = UserLocalDatasourceImpl(dao, mockk(relaxed = true))
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given an empty storage when user cached then user is stored successfully`() = runTest {
        val localUserName = "Draco"

        val sut = UserLocalDatasourceImpl(provideFakeDao(), mockk(relaxed = true))
        sut.saveUser(UserLocalMock.provideUser(name = localUserName).toUserDataModel())
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given a storage with user when user cached then old user is removed and new user is stored successfully`() = runTest {
        val localUserName = "Draco"
        fakeStorage = UserLocalMock.provideUser()

        val sut = UserLocalDatasourceImpl(provideFakeDao(), mockk(relaxed = true))
        sut.saveUser(UserLocalMock.provideUser(name = localUserName).toUserDataModel())
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given a storage with user when is requested then is returned successfully`() = runTest {
        val localUserName = "Draco"
        fakeStorage = UserLocalMock.provideUser(name = localUserName)

        val sut = UserLocalDatasourceImpl(provideFakeDao(), mockk(relaxed = true))
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given a storage with user when logout then user and cached events are cleared`() = runTest {
        val eventDao = provideFakeEventDao()
        val userDao = provideFakeDao()
        val sut = UserLocalDatasourceImpl(userDao, eventDao)
        sut.saveUser(UserLocalMock.provideUser().toUserDataModel())

        sut.logout()

        coVerify(exactly = 1) { eventDao.deleteAll() }
        Assert.assertTrue(eventDataCleared)
        Assert.assertTrue(runCatching { sut.getUser() }.isFailure)
    }


}