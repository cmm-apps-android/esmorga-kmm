package cmm.apps.datasource.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import cmm.apps.datasource.local.database.dao.EventDao
import cmm.apps.datasource.local.database.dao.UserDao
import cmm.apps.datasource.local.event.model.EventLocalModel
import cmm.apps.datasource.local.user.model.UserLocalModel

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

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object EsmorgaDatabaseConstructor : RoomDatabaseConstructor<EsmorgaDatabase> {
    override fun initialize(): EsmorgaDatabase
}
