package io.mvlchain.mvlswap.boundary.dto

import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class SwapBep2ToErc20RequestDto (

    @NotNull
    val bep2SwapID: String
)
