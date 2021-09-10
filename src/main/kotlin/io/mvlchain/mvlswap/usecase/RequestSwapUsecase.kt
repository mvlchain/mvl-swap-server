package io.mvlchain.mvlswap.usecase

import io.mvlchain.mvlswap.boundary.dto.SwapRequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapResponeDto
import org.springframework.stereotype.Component

@Component
class RequestSwapUsecase {
    fun execute(swapRequestDto: SwapRequestDto): SwapResponeDto {
        TODO("implement")
        /*

        val lNow = System.currentTimeMillis()
        val In_amount = swapRequestDto.outAmount!!.subtract(BigDecimal("100"))
        val swap = SWAP()
        swap.deputyOutAmount = swapRequestDto.outAmount!!.subtract(BigDecimal("100")).toPlainString()
        swap.erc20ChainAddr = "0xA1805D94419b88e30F88bD3Ab3bC618610805f26"
        swap.inAmount = In_amount.toString()
        swap.outAmount = swapRequestDto.outAmount!!.toPlainString()
        swap.randomNumberHash = swapRequestDto.randomNumberHash
        swap.receiverAddr = swapRequestDto.bep2RecipientAddr
        swap.refundAddr = swapRequestDto.refundAddr
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
         */
    }
}