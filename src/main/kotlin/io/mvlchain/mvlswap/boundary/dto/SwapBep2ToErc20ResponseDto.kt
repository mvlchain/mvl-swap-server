package io.mvlchain.mvlswap.boundary.dto

import org.jetbrains.annotations.NotNull

data class SwapBep2ToErc20ResponseDto(

    @NotNull
    val erc20SwapID: String,
    @NotNull
    val erc20TxHash: String
)
