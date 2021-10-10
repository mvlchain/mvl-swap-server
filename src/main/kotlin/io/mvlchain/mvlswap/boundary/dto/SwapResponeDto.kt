package io.mvlchain.mvlswap.boundary.dto

import java.math.BigDecimal

data class SwapResponeDto(
    val amount: BigDecimal,
    val depositAddress: String
)
