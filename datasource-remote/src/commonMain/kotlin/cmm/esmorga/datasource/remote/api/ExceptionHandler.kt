package cmm.esmorga.datasource.remote.api

import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Source
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.parsing.ParseException
import kotlinx.io.IOException

object ExceptionHandler {
    fun manageApiException(e: Throwable): EsmorgaException {
        when (e) {
            is ClientRequestException -> throw EsmorgaException(message = e.response.status.description, source = Source.REMOTE, code = e.response.status.value)
            is ServerResponseException -> throw EsmorgaException(message = e.response.status.description, source = Source.REMOTE, code = e.response.status.value)
            is ParseException -> throw EsmorgaException(message = "Response error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.PARSE_ERROR)
            is IOException -> throw EsmorgaException(message = "No connection error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.NO_CONNECTION)
            is EsmorgaException -> throw e
            else -> throw EsmorgaException(message = "Unexpected error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR)
        }
    }
}