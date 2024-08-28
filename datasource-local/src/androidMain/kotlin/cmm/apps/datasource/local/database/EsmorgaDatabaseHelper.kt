package cmm.apps.datasource.local.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object EsmorgaDatabaseHelper : KoinComponent {
    private val context: Context by inject()
    fun getDatabase(): EsmorgaDatabase {
        val dbFile = context.getDatabasePath("esmorga.db")
        return Room.databaseBuilder<EsmorgaDatabase>(
            context = context,
            name = dbFile.absolutePath
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO).build()
    }
}