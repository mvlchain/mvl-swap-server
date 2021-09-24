package io.mvlchain.mvlswap.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class MvlBaseWebException(
    internalMessage: String,
    httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val code: String,
    val messageArgs: Array<out Any>? = null,
    val reportSentry: Boolean = false,
    val fallbackCode: String? = null,
    private val fillInStackTrace: Boolean = false,
) : ResponseStatusException(httpStatus, internalMessage) {
    open fun errorInfo(): Map<String, Any?> = emptyMap()

    override fun fillInStackTrace(): Throwable {
        return if (fillInStackTrace)
            super.fillInStackTrace()
        else
            this
    }
}
