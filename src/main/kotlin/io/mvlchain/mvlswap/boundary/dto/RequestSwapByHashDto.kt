package io.mvlchain.mvlswap.boundary.dto

import java.math.BigDecimal

data class RequestSwapByHashDto (
    val outAmount: BigDecimal? = null,
    val randomNumberHash: String? = null,
    val refundAddr: String? = null,
    val bep2RecipientAddr: String? = null,
)
