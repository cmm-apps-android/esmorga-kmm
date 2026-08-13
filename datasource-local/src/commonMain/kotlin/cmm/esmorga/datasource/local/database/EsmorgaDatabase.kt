package cmm.esmorga.datasource.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import cmm.esmorga.datasource.local.database.dao.EventDao
import cmm.esmorga.datasource.local.database.dao.UserDao
import cmm.esmorga.datasource.local.event.model.EventLocalModel
import cmm.esmorga.datasource.local.user.model.UserLocalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        EventLocalModel::class,
        UserLocalModel::class
    ], version = 1, exportSchema = true
)
@TypeConverters(ZonedDateTimeConverter::class)
@ConstructedBy(EsmorgaDatabaseConstructor::class)
abstract class EsmorgaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
}

fun buildDatabase(builder: RoomDatabase.Builder<EsmorgaDatabase>): EsmorgaDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object EsmorgaDatabaseConstructor : RoomDatabaseConstructor<EsmorgaDatabase> {
    override fun initialize(): EsmorgaDatabase
}
