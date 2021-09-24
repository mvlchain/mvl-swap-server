package io.mvlchain.mvlswap.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    enumAsRef = true,
    description = "SwapStatus" +
            "\n- REQUESTED (swap is requested, but not deposited yet) " +
            "\n- DEPOSITED (MVL is deposited to deputy address) " +
            "\n- CLAIMED (swap claimed, finished normally) " +
            "\n- REFUNDED (MVL is refunded to refund address) "
)
enum class SwapStatus {
    REQUESTED,
    DEPOSITED,
    CLAIMED,
    REFUNDED
}
