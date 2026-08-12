package cmm.apps.datasource.local.database

import androidx.room.Room
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual object EsmorgaDatabaseHelper {
    @OptIn(ExperimentalForeignApi::class)
    actual fun getDatabase(): EsmorgaDatabase {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val dbFilePath = documentDirectory?.path + "/esmorga.db"
        return buildDatabase(
            Room.databaseBuilder<EsmorgaDatabase>(
                name = dbFilePath
            )
        )
    }
}