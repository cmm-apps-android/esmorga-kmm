package cmm.apps.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
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
abstract class EsmorgaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
}