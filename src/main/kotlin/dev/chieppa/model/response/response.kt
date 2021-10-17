package dev.chieppa.model.response

import dev.chieppa.model.exception.ExceptionType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

@Serializable
enum class ResponseType {
    EMPTY,
    ARRAY,
    ERROR,
    SUCCESS,
    MESSAGE,
}

@Serializable
abstract class BasicResponse {
    abstract val type: ResponseType
}

@Serializable
data class ArrayResponse(
    override val type: ResponseType = ResponseType.ARRAY,
    val array: JsonArray
) : BasicResponse()

@Serializable
data class SuccessResponse(
    override val type: ResponseType = ResponseType.SUCCESS
) : BasicResponse()

@Serializable
data class InformationalResponse(
    override val type: ResponseType = ResponseType.MESSAGE,
    val message: String,
) : BasicResponse()


@Serializable
data class ErrorResponse(
    override val type: ResponseType = ResponseType.ERROR,
    val errorType: ExceptionType,
    val error: JsonElement
) : BasicResponse()