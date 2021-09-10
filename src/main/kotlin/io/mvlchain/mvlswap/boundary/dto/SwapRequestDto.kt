package io.mvlchain.mvlswap.boundary.dto

import java.math.BigDecimal

data class SwapRequestDto (
    val outAmount: BigDecimal? = null,
    val randomNumberHash: String? = null,
    val timestamp: Long = 0,
    val refundAddr: String? = null,
    val bep2RecipientAddr: String? = null,
    val erc20SenderAddr: String? = null
)
