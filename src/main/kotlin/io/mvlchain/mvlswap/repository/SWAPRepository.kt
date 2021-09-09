package io.mvlchain.mvlswap.repository

import io.mvlchain.mvlswap.model.SWAP
import org.springframework.data.repository.CrudRepository

interface SWAPRepository: CrudRepository<SWAP, Long> {
    override fun count(): Long

    fun findByRandomNumberHash(randomNumberHash: String?): SWAP?
}