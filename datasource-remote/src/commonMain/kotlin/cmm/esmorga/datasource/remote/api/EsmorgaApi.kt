package cmm.esmorga.datasource.remote.api

import cmm.esmorga.datasource.remote.event.model.EventAttendeeWrapperRemoteModel
import cmm.esmorga.datasource.remote.event.model.EventListWrapperRemoteModel
import cmm.esmorga.datasource.remote.user.model.AccessTokenRemoteModel
import cmm.esmorga.datasource.remote.user.model.ChangePasswordBodyRemoteModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class EsmorgaApi(private val httpClient: HttpClient) {

    suspend fun getMyEvents(): EventListWrapperRemoteModel {
        val response = httpClient.get("account/events")
        return response.body()
    }

    suspend fun joinEvent(eventId: String) {
        httpClient.post("account/events") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("eventId" to eventId))
        }
    }

    suspend fun leaveEvent(eventId: String) {
        httpClient.delete("account/events") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("eventId" to eventId))
        }
    }

    suspend fun changePassword(body: ChangePasswordBodyRemoteModel): AccessTokenRemoteModel {
        val response = httpClient.put("account/password") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body()
    }

    suspend fun getEventAttendees(eventId: String): EventAttendeeWrapperRemoteModel {
        val response = httpClient.get("events/$eventId/users")
        return response.body()
    }

    fun clearTokens() {
        httpClient.clearAuthTokens()
    }

}