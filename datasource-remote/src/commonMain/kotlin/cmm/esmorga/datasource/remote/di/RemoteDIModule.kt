package cmm.esmorga.datasource.remote.di

import cmm.esmorga.data.di.DataDIModule
import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.datasource.remote.api.EsmorgaApi
import cmm.esmorga.datasource.remote.api.EsmorgaPublicApi
import cmm.esmorga.datasource.remote.api.NetworkApiHelper
import cmm.esmorga.datasource.remote.event.EventRemoteDatasourceImpl
import cmm.esmorga.datasource.remote.user.UserRemoteDatasourceImpl
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

object RemoteDIModule {

    private val HTTP_CLIENT_PUBLIC = named("HttpClientPublic")
    private val HTTP_CLIENT_AUTHENTICATED = named("HttpClientAuthenticated")

    val module = module {
        factory<EventDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { EventRemoteDatasourceImpl(get(), get()) }
        factory<UserDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { UserRemoteDatasourceImpl(get(), get()) }
        
        single<HttpClient>(HTTP_CLIENT_PUBLIC) {
            NetworkApiHelper().providePublicApi(
                baseUrl = "https://qa.api.esmorgaevents.com/v1/"
            )
        }
        
        single<HttpClient>(HTTP_CLIENT_AUTHENTICATED) {
            NetworkApiHelper().provideAuthenticatedApi(
                baseUrl = "https://qa.api.esmorgaevents.com/v1/",
                userLocalDs = get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))
            )
        }
        
        single<EsmorgaPublicApi> { EsmorgaPublicApi(get(HTTP_CLIENT_PUBLIC)) }
        single<EsmorgaApi> { EsmorgaApi(get(HTTP_CLIENT_AUTHENTICATED)) }
    }

}