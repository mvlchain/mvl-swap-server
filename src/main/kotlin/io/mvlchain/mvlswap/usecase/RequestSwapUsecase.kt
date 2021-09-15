package io.mvlchain.mvlswap.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import io.mvlchain.mvlswap.boundary.dto.SwapRequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapResponeDto
import io.mvlchain.mvlswap.model.SwapHistory
import io.mvlchain.mvlswap.model.SwapStatus
import io.mvlchain.mvlswap.repository.SwapHistoryRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class RequestSwapUsecase(
    private val swapHistoryRepository: SwapHistoryRepository
) {

    fun execute(swapRequestDto: SwapRequestDto): SwapResponeDto {

        val lNow = System.currentTimeMillis()
        val In_amount = swapRequestDto.outAmount.subtract(BigDecimal("100"))
        val swap = SwapHistory()
        swap.deputyOutAmount = swapRequestDto.outAmount.subtract(BigDecimal("100")).toPlainString()
        swap.erc20ChainAddr = "0xA1805D94419b88e30F88bD3Ab3bC618610805f26"
        swap.inAmount = In_amount.toString()
        swap.outAmount = swapRequestDto.outAmount.toPlainString()
        swap.randomNumberHash = swapRequestDto.randomNumberHash
        swap.receiverAddr = swapRequestDto.bep2RecipientAddr
        swap.refundAddr = swapRequestDto.refundAddr
        swap.senderAddr = "0x0000000000000000000000000000000000000000"
        swap.status = "REQUESTED"
        swap.timestamp = lNow
        swap.type = "1s"

        swapHistoryRepository.save(swap)

        return SwapResponeDto(depositAddress = "0x2E653AEf53656fd39E44aF28090d27BD1F6c5984", amount = In_amount)

    }
}



