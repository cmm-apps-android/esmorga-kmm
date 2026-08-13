package cmm.esmorga.datasource.local.di

import cmm.esmorga.data.di.DataDIModule
import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.datasource.local.database.EsmorgaDatabase
import cmm.esmorga.datasource.local.database.EsmorgaDatabaseHelper
import cmm.esmorga.datasource.local.database.dao.EventDao
import cmm.esmorga.datasource.local.database.dao.UserDao
import cmm.esmorga.datasource.local.event.EventLocalDatasourceImpl
import cmm.esmorga.datasource.local.user.UserLocalDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LocalDIModule {

    val module = module {
        single<EsmorgaDatabase> {
            EsmorgaDatabaseHelper.getDatabase()
        }
        single<EventDao> { get<EsmorgaDatabase>().eventDao() }
        factory<EventDatasource>(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME)) { EventLocalDatasourceImpl(get()) }

        single<UserDao> { get<EsmorgaDatabase>().userDao() }
        factory<UserDatasource>(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME)) { UserLocalDatasourceImpl(get()) }
    }

}