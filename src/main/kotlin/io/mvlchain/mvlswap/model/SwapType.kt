package io.mvlchain.mvlswap.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    enumAsRef = true,
    description = "SwapType" +
            "\n- TO_BEP2: ERC -> BEP (Ethereum -> Binance)" +
            "\n- TO_ERC20: BEP -> ERC (Binance -> Ethereum)"
)
enum class SwapType {
    TO_BEP2,
    TO_ERC20
}