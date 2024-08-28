package database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import cmm.apps.datasource.local.database.EsmorgaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSHomeDirectory


object EsmorgaDatabaseHelper {
    fun getDatabase(): EsmorgaDatabase {
        val dbFilePath = NSHomeDirectory() + "/esmorga.db"
        return Room.databaseBuilder<EsmorgaDatabase>(
            name = dbFilePath,
            factory = { EsmorgaDatabase::class.instantiateImpl() }
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO).build()
    }
}