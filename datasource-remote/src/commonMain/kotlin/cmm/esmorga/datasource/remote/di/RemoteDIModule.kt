package cmm.esmorga.datasource.remote.di

import cmm.esmorga.data.di.DataDIModule
import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.datasource.remote.api.EsmorgaApi
import cmm.esmorga.datasource.remote.api.NetworkApiHelper
import cmm.esmorga.datasource.remote.event.EventRemoteDatasourceImpl
import cmm.esmorga.datasource.remote.user.UserRemoteDatasourceImpl
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

object RemoteDIModule {

    val module = module {
        factory<EventDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { EventRemoteDatasourceImpl(get()) }
        factory<UserDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { UserRemoteDatasourceImpl(get()) }
        single<HttpClient> {
            NetworkApiHelper().provideApi(
                baseUrl = "https://qa.api.esmorgaevents.com/v1/",
                userLocalDs = get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))
            )
        }
        single<EsmorgaApi> { EsmorgaApi(get()) }
    }

}