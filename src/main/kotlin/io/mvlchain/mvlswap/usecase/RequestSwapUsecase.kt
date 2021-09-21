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


    companion object {
        private val Fee:String="0"
        private val DeputyErc20Address:String="0x0000000000000000000000000000000000000000"
        private val DeputyBep2Address:String="0x0000000000000000000000000000000000000000"

    }

    fun execute(swapRequestDto: SwapRequestDto): SwapResponeDto {

        val fee:String = "0"

        val In_amount = swapRequestDto.outAmount!!.subtract(BigDecimal(Fee))

        val swap = SwapHistory()

        swap.erc20SenderAddr = swapRequestDto.erc20SenderAddr
        swap.deputyOutAmount = swapRequestDto.outAmount!!.subtract(BigDecimal(Fee)).toPlainString()
        swap.erc20ChainAddr = DeputyErc20Address
        swap.inAmount = In_amount.toString()
        swap.outAmount = swapRequestDto.outAmount!!.toPlainString()
        swap.randomNumberHash = swapRequestDto.randomNumberHash
        swap.receiverAddr = swapRequestDto.bep2RecipientAddr
        swap.senderAddr = DeputyBep2Address
        swap.status = "REQUESTED"
        swap.timestamp = swapRequestDto.timestamp
        swap.type = "1s"
        swap.expireHeight = 1000
        swapHistoryRepository!!.save(swap)

        return SwapResponeDto(depositAddress = DeputyErc20Address, amount = In_amount)
    }
}



