package io.mvlchain.mvlswap.boundary.dto

data class SwapDepositResponseDto(
    val erc20SwapID: String,
    val erc20TxHash: String,
    val bep2SwapID: String,
    val bep2TxHash: String
)
