package io.mvlchain.mvlswap.boundary.dto

import io.mvlchain.mvlswap.model.SwapStatus
import io.mvlchain.mvlswap.model.SwapType

data class SwapHistoryResponseDto(
    val hash: String,
    val type: SwapType,
    val status: SwapStatus
)
