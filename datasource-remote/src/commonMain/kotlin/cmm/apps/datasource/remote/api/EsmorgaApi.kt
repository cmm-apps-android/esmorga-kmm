package cmm.apps.datasource.remote.api

import cmm.apps.datasource.remote.event.model.EventListWrapperRemoteModel
import cmm.apps.datasource.remote.user.model.UserRemoteModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
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

    suspend fun register(body: Map<String, String>): UserRemoteModel {
        val response = httpClient.post("account/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body()
    }

}