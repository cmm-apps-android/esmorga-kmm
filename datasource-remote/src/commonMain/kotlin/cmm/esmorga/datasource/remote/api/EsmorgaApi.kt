package cmm.esmorga.datasource.remote.api

import cmm.esmorga.datasource.remote.event.model.EventListWrapperRemoteModel
import cmm.esmorga.datasource.remote.user.model.AccessTokenRemoteModel
import cmm.esmorga.datasource.remote.user.model.ChangePasswordBodyRemoteModel
import cmm.esmorga.datasource.remote.user.model.UserRemoteModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class EsmorgaApi(private val httpClient: HttpClient) {
//"https://qa.esmorga.canarte.org/v1/"

    suspend fun login(body: Map<String, String>): UserRemoteModel {
        val response = httpClient.post("account/login") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body()
    }

    suspend fun getEvents(): EventListWrapperRemoteModel {
        val response = httpClient.get("events")
        return response.body()
    }

    suspend fun getMyEvents(): EventListWrapperRemoteModel {
        val response = httpClient.get("account/events")
        return response.body()
    }

    suspend fun register(body: Map<String, String>): UserRemoteModel {
        val response = httpClient.post("account/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body()
    }

    suspend fun changePassword(body: ChangePasswordBodyRemoteModel): AccessTokenRemoteModel {
        val response = httpClient.put("account/password") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body()
    }

}