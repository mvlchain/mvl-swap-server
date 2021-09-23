package io.mvlchain.mvlswap.boundary.dto

import java.math.BigDecimal

data class SwapDepositResponeDto(
    val erc20SwapID: String,
    val erc20TxHash: String,
    val bep2SwapID: String,
    val bep2TxHash: String
)