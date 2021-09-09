package io.mvlchain.mvlswap.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mvlchain.mvlswap.model.MVLSwapRequest
import io.mvlchain.mvlswap.model.RequestSwapByRandomNumberHash
import io.mvlchain.mvlswap.model.SWAP
import io.mvlchain.mvlswap.repository.SWAPRepository
import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.math.BigDecimal
import java.util.concurrent.ExecutionException

@RestController
class erc20ToBep2 {
    @Qualifier("SWAPRepository")
    @Autowired
    private val swapRepository: SWAPRepository? = null

    private val GETPROVIDER_DEV: String = ETHProvider.getAPIHost("ETH");

    @PostMapping("/requestSwapByRandomNumberHash")
    @Throws(
        ExecutionException::class,
        InterruptedException::class,
        IOException::class
    )
    fun requestSwapByRandomNumberHash(
        @RequestBody requestSwapByRandomNumberHash: RequestSwapByRandomNumberHash,
        model: Model?
    ): String? {
        //<-- 예외처리 추가
        val swap =
            swapRepository!!.findByRandomNumberHash(requestSwapByRandomNumberHash.randomNumberHash)
        val mapper = ObjectMapper()
        val map: MutableMap<String, Any?> = HashMap()
        map["result"] = 1
        map["swap"] = swap
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)
    }

    @PostMapping("/requestSwap")
    @Throws(ExecutionException::class, InterruptedException::class, IOException::class)
    fun requestSwap(
        @RequestBody mvlSwapRequest: MVLSwapRequest,
        model: Model?
    ): String? {

        val lNow = System.currentTimeMillis()
        val In_amount = mvlSwapRequest.outAmount!!.subtract(BigDecimal("100"))
        val swap = SWAP()
        swap.deputyOutAmount = mvlSwapRequest.outAmount!!.subtract(BigDecimal("100")).toPlainString()
        swap.erc20ChainAddr = "0xA1805D94419b88e30F88bD3Ab3bC618610805f26"
        swap.inAmount = In_amount.toString()
        swap.outAmount = mvlSwapRequest.outAmount!!.toPlainString()
        swap.randomNumberHash = mvlSwapRequest.randomNumberHash
        swap.receiverAddr = mvlSwapRequest.bep2RecipientAddr
        swap.refundAddr = mvlSwapRequest.refundAddr
        swap.senderAddr = "0x0000000000000000000000000000000000000000"
        swap.status = "1"
        swap.timestamp = lNow
        swap.type = "1s"

        swapRepository!!.save(swap)

        val mapper = ObjectMapper()
        val map: MutableMap<String, Any> = HashMap()
        map["result"] = 1
        map["In_amount"] = In_amount
        map["Receiver_addr"] = "0x2E653AEf53656fd39E44aF28090d27BD1F6c5984"
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)
    }


}