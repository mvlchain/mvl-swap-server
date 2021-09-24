package io.mvlchain.mvlswap.boundary.dto

import java.math.BigDecimal

data class SwapResponseDto(
    val amount: BigDecimal,
    val depositAddress: String
)