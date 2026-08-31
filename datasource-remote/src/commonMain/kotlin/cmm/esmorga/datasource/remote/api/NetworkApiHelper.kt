package cmm.esmorga.datasource.remote.api

import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.datasource.remote.user.model.UserRemoteModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class NetworkApiHelper {

    fun provideApi(baseUrl: String, userLocalDs: UserDatasource): HttpClient = provideHttpClient(baseUrl, userLocalDs)

    private fun provideHttpClient(baseUrl: String, userLocalDs: UserDatasource): HttpClient {
        val refreshClient = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                url(baseUrl)
            }
        }

        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        try {
                            val user = userLocalDs.getUser()
                            // Proactive refresh if token expires in less than 60 seconds
                            if (user.dataExpiresAt < Clock.System.now().toEpochMilliseconds() + 60_000) {
                                performTokenRefresh(refreshClient, userLocalDs)
                            } else {
                                BearerTokens(user.dataAccessToken, user.dataRefreshToken)
                            }
                        } catch (_: Exception) {
                            null
                        }
                    }

                    refreshTokens {
                        performTokenRefresh(refreshClient, userLocalDs)
                    }

                    sendWithoutRequest { request ->
                        request.url.encodedPath.endsWith("account/events")
                    }
                }
            }
            defaultRequest {
                url(baseUrl)
            }
        }
    }

    private suspend fun performTokenRefresh(refreshClient: HttpClient, userLocalDs: UserDatasource): BearerTokens? {
        return try {
            val user = userLocalDs.getUser()
            val response = refreshClient.post("account/refresh") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("refreshToken" to user.dataRefreshToken))
            }.body<UserRemoteModel>()

            val updatedUser = user.copy(
                dataAccessToken = response.remoteAccessToken,
                dataRefreshToken = response.remoteRefreshToken,
                dataExpiresAt = Clock.System.now().toEpochMilliseconds() + (response.ttl * 1000L)
            )
            userLocalDs.saveUser(updatedUser)

            BearerTokens(updatedUser.dataAccessToken, updatedUser.dataRefreshToken)
        } catch (_: Exception) {
            null
        }
    }
}
