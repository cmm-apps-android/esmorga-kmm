package cmm.esmorga.datasource.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import cmm.esmorga.datasource.local.database.dao.AttendeeDao
import cmm.esmorga.datasource.local.database.dao.EventDao
import cmm.esmorga.datasource.local.database.dao.UserDao
import cmm.esmorga.datasource.local.event.model.AttendeeLocalModel
import cmm.esmorga.datasource.local.event.model.EventLocalModel
import cmm.esmorga.datasource.local.user.model.UserLocalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        EventLocalModel::class,
        UserLocalModel::class,
        AttendeeLocalModel::class
    ], version = 4, exportSchema = true
)
@TypeConverters(ZonedDateTimeConverter::class)
@ConstructedBy(EsmorgaDatabaseConstructor::class)
abstract class EsmorgaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun attendeeDao(): AttendeeDao
}

fun buildDatabase(builder: RoomDatabase.Builder<EsmorgaDatabase>): EsmorgaDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object EsmorgaDatabaseConstructor : RoomDatabaseConstructor<EsmorgaDatabase> {
    override fun initialize(): EsmorgaDatabase
}
