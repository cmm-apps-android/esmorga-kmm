package cmm.apps.datasource.local.di

import cmm.apps.data.di.DataDIModule
import cmm.apps.data.event.datasource.EventDatasource
import cmm.apps.data.user.datasource.UserDatasource
import cmm.apps.datasource.local.database.EsmorgaDatabase
import cmm.apps.datasource.local.database.dao.EventDao
import cmm.apps.datasource.local.database.dao.UserDao
import cmm.apps.datasource.local.event.EventLocalDatasourceImpl
import cmm.apps.datasource.local.user.UserLocalDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect fun getDatabase(): EsmorgaDatabase

object LocalDIModule {

    val module = module {
        single<EsmorgaDatabase> {
            getDatabase()
        }
        single<EventDao> { get<EsmorgaDatabase>().eventDao() }
        factory<EventDatasource>(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME)) { EventLocalDatasourceImpl(get()) }

        single<UserDao> { get<EsmorgaDatabase>().userDao() }
        factory<UserDatasource>(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME)) { UserLocalDatasourceImpl(get()) }
    }

}