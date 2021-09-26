package io.mvlchain.mvlswap.boundary.dto

import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class SwapErc20ToBep2RequestDto (
    @NotNull
    val erc20OutAmountFromSender: BigDecimal,
    @NotNull
    val randomNumberHash: String,
    @NotNull
    val timestamp: Long,
    @NotNull
    val bep2RecipientAddr: String,
    @NotNull
    val erc20SenderAddr: String,
    @NotNull
    val signature: String
)
