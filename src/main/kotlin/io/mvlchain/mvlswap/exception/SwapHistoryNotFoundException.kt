package io.mvlchain.mvlswap.exception

import org.springframework.http.HttpStatus

class SwapHistoryNotFoundException: MvlBaseWebException {
    constructor(hash: String) : super(internalMessage = "Cannot find swap history with hash=$hash", httpStatus = HttpStatus.NOT_FOUND, code = "not_found")
    constructor(id: Long) : super(internalMessage = "Cannot find swap history with id=$id", httpStatus = HttpStatus.NOT_FOUND, code = "not_found")
}