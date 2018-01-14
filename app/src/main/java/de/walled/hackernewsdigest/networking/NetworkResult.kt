package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.HackerNewsItem
import de.walled.hackernewsdigest.data.OptionalArticle

sealed class NetworkResult {

    companion object {
        fun fromAggregateResponse(payload: List<Long>): NetworkResult {
            return Payload.AggregateArticles(payload)
        }

        fun fromArticleResponse(payload: OptionalArticle): NetworkResult {
            return Payload.SingleArticle(payload)
        }

        fun fromErrorResponse(code: Int, message: String?): NetworkResult {
            return when (code) {
                404 -> HttpError.ResourceNotFound
                503 -> HttpError.ServiceUnavailable
                else -> HttpError.UnknownError(code, message ?: "")
            }
        }
    }
}

sealed class Payload : NetworkResult() {

    data class AggregateArticles(val articleIds: List<Long>) : Payload()

    data class SingleArticle(val article: OptionalArticle) : Payload()
}

sealed class HttpError : NetworkResult() {

    object ResourceNotFound : HttpError()

    object ServiceUnavailable : HttpError()

    data class UnknownError(val code: Int, val message: String) : HttpError()
}