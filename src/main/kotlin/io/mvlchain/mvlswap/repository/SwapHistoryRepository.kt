package io.mvlchain.mvlswap.repository

import io.mvlchain.mvlswap.model.SwapHistory
import org.springframework.data.repository.CrudRepository

interface SwapHistoryRepository: CrudRepository<SwapHistory, Long> {
    fun findByRandomNumberHash(randomNumberHash: String): SwapHistory?
}