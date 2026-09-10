package cmm.esmorga.data.di

import cmm.esmorga.data.event.EventRepositoryImpl
import cmm.esmorga.data.user.UserRepositoryImpl
import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.user.repository.UserRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module


object DataDIModule {

    const val LOCAL_DATASOURCE_INSTANCE_NAME = "LocalDatasourceInstance"
    const val REMOTE_DATASOURCE_INSTANCE_NAME = "RemoteDatasourceInstance"

    val module = module {
        factory<EventRepository> { EventRepositoryImpl(get(named(LOCAL_DATASOURCE_INSTANCE_NAME)), get(named(LOCAL_DATASOURCE_INSTANCE_NAME)), get(named(REMOTE_DATASOURCE_INSTANCE_NAME))) }
        factory<UserRepository> { UserRepositoryImpl(get(named(LOCAL_DATASOURCE_INSTANCE_NAME)), get(named(REMOTE_DATASOURCE_INSTANCE_NAME)), get(named(LOCAL_DATASOURCE_INSTANCE_NAME))) }
    }

}