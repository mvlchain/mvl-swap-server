package io.mvlchain.mvlswap.repository

import io.mvlchain.mvlswap.model.SwapHistory
import org.springframework.data.repository.CrudRepository

interface SwapHistoryRepository : CrudRepository<SwapHistory, Long> {
    fun findByRandomNumberHash(randomNumberHash: String): SwapHistory?

    // TODO: 네이밍 변경
    fun findByErc20SenderAddr(erc20SenderAddr: String): SwapHistory?
}
