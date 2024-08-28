package cmm.apps.datasource.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NetworkApiHelper {

    fun provideApi(baseUrl: String): HttpClient = provideHttpClient(baseUrl)

    private fun provideHttpClient(baseUrl: String): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            defaultRequest {
                url(baseUrl)
            }
        }
    }
}