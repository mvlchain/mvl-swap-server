package io.mvlchain.mvlswap.boundary.dto

import io.mvlchain.mvlswap.model.SwapStatus

data class SwapHistoryResponseDto(
    val hash: String,
    val type: String,
    val status: SwapStatus
)
