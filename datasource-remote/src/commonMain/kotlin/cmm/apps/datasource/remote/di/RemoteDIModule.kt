package cmm.apps.datasource.remote.di

import cmm.apps.datasource.remote.api.EsmorgaApi
import cmm.apps.datasource.remote.event.EventRemoteDatasourceImpl
import cmm.apps.datasource.remote.user.UserRemoteDatasourceImpl
import cmm.apps.data.di.DataDIModule
import cmm.apps.data.event.datasource.EventDatasource
import cmm.apps.data.user.datasource.UserDatasource
import cmm.apps.datasource.remote.api.NetworkApiHelper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

object RemoteDIModule {

    val module = module {
        factory<EventDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { EventRemoteDatasourceImpl(get()) }
        factory<UserDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { UserRemoteDatasourceImpl(get()) }
        single<HttpClient> {
            NetworkApiHelper().provideApi("https://qa.esmorga.canarte.org/v1/")
        }
        single<EsmorgaApi> { EsmorgaApi(get()) }
    }

}