package io.mvlchain.mvlswap.boundary.dto

import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class SwapBep2ToErc20RequestDto (

    @NotNull
    val bep2OutAmount: BigDecimal,
    @NotNull
    val randomNumberHash: String,
    @NotNull
    val timestamp: Long,
    @NotNull
    val erc20RecipientAddr: String,
    @NotNull
    val bep2SenderAddr: String,
    @NotNull
    val signature: String
)
