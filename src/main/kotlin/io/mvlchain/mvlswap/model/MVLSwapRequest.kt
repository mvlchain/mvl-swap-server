package io.mvlchain.mvlswap.model

import java.math.BigDecimal

class MVLSwapRequest {
    var outAmount: BigDecimal? = null
    var randomNumberHash: String? = null
    var timestamp: Long = 0
    var refundAddr: String? = null
    var bep2RecipientAddr: String? = null
    var erc20SenderAddr: String? = null
}