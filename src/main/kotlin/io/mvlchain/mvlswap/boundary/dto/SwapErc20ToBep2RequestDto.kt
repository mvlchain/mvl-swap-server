package io.mvlchain.mvlswap.boundary.dto

import io.mvlchain.mvlswap.model.SwapType
import io.swagger.v3.oas.annotations.media.Schema
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class SwapErc20ToBep2RequestDto (
    @NotNull
    val erc20OutAmountFromSender: BigDecimal,
    @NotNull
    val randomNumberHash: String,

    @Schema(description = "randomNumberHash 생성시 사용된 timestamp")
    @NotNull
    val timestamp: Long,

    @Schema(description = "swap 후에 바뀐 MVL을 받을 지갑")
    @NotNull
    val bep2RecipientAddr: String,
    @NotNull
    val publicKey: String,

    @NotNull
    val type: SwapType
)
