package io.mvlchain.mvlswap.model

import java.math.BigDecimal

class RequestSwapByRandomNumberHash {
    var outAmount: BigDecimal? = null
    var randomNumberHash: String? = null
    var refundAddr: String? = null
    var bep2RecipientAddr: String? = null
}