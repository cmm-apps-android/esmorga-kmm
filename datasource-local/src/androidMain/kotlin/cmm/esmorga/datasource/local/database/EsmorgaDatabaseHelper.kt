package cmm.esmorga.datasource.local.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual object EsmorgaDatabaseHelper : KoinComponent {
    private val context: Context by inject()
    actual fun getDatabase(): EsmorgaDatabase {
        val dbFile = context.getDatabasePath("esmorga.db")
        return buildDatabase(
            Room.databaseBuilder<EsmorgaDatabase>(
                context = context,
                name = dbFile.absolutePath
            )
        )
    }
}