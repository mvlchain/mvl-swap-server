package io.mvlchain.mvlswap.boundary.dto

import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class SwapRequestDto (
    @NotNull
    val outAmount: BigDecimal,
    @NotNull
    val randomNumberHash: String,
    @NotNull
    val timestamp: Long,
    @NotNull
    val refundAddr: String,
    @NotNull
    val bep2RecipientAddr: String,
    @NotNull
    val erc20SenderAddr: String,
    @NotNull
    val signature: String
)
