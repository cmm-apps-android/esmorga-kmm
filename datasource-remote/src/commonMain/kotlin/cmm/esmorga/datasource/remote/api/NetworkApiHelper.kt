package cmm.esmorga.datasource.remote.api

import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.data.user.model.UserDataModel
import cmm.esmorga.datasource.remote.user.model.AccessTokenRemoteModel
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class NetworkApiHelper {

    private val customLogger = object : Logger {
        override fun log(message: String) {
            println("KtorHttpClient: $message")
        }
    }

    private val jsonConfiguration = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    fun providePublicApi(baseUrl: String): HttpClient = createHttpClient(baseUrl)

    fun provideAuthenticatedApi(baseUrl: String, userLocalDs: UserDatasource): HttpClient {
        val refreshClient = createHttpClient(baseUrl)

        return createHttpClient(baseUrl) {
            install(Auth) {
                bearer {
                    loadTokens {
                        resolveBearerTokens(userLocalDs, refreshClient)
                    }

                    refreshTokens {
                        refreshBearerTokens(userLocalDs, refreshClient)
                    }
                }
            }
        }
    }

    private fun createHttpClient(
        baseUrl: String,
        configure: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {
        return HttpClient {
            install(Logging) {
                logger = customLogger
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(jsonConfiguration)
            }
            defaultRequest {
                url(baseUrl)
            }
            configure()
        }
    }

    private suspend fun resolveBearerTokens(
        userLocalDs: UserDatasource,
        refreshClient: HttpClient
    ): BearerTokens? {
        val user = runCatching { userLocalDs.getUser() }.getOrNull() ?: return null

        return if (user.shouldRefreshToken()) {
            performTokenRefresh(refreshClient, userLocalDs, user)
        } else {
            user.toBearerTokens()
        }
    }

    private suspend fun refreshBearerTokens(
        userLocalDs: UserDatasource,
        refreshClient: HttpClient
    ): BearerTokens? = performTokenRefresh(refreshClient, userLocalDs)

    private suspend fun performTokenRefresh(
        refreshClient: HttpClient,
        userLocalDs: UserDatasource,
        user: UserDataModel? = null
    ): BearerTokens? {
        return runCatching {
            val currentUser = user ?: userLocalDs.getUser()
            val response = refreshClient.post("account/refresh") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("refreshToken" to currentUser.dataRefreshToken))
            }.body<AccessTokenRemoteModel>()

            val updatedUser = currentUser.copy(
                dataAccessToken = response.accessToken,
                dataRefreshToken = response.refreshToken,
                dataExpiresAt = Clock.System.now().toEpochMilliseconds() + (response.ttl * 1000L)
            )
            userLocalDs.saveUser(updatedUser)

            updatedUser.toBearerTokens()
        }.getOrNull()
    }

    private fun UserDataModel.shouldRefreshToken(
        currentTimeMillis: Long = Clock.System.now().toEpochMilliseconds()
    ): Boolean = dataExpiresAt < currentTimeMillis + TOKEN_REFRESH_THRESHOLD_MS

    private fun UserDataModel.toBearerTokens(): BearerTokens =
        BearerTokens(dataAccessToken, dataRefreshToken)

    private companion object {
        const val TOKEN_REFRESH_THRESHOLD_MS = 60_000L
    }
}
