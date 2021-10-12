package io.mvlchain.mvlswap.usecase

import io.mvlchain.mvlswap.boundary.dto.SwapErc20ToBep2RequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapResponeDto
import io.mvlchain.mvlswap.model.SwapHistory
import io.mvlchain.mvlswap.repository.SwapHistoryRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class RequestErc20ToBep2SwapUsecase(

    private val swapHistoryRepository: SwapHistoryRepository
) {

    companion object {
        private val Fee: String = "0"
        private val DeputyErc20Address: String = "0xd2E5C581A406F28a3DC8Ab0313B68950B58dd4AD"
        private val DeputyBep2Address: String = "tbnb15fe7p6tsaf6gghpgdd4tdqm6kwtryd60rnt2s8"
        private val Erc20BasicUnit = BigDecimal("1000000000000000000")
        private val Bep2BacicUnit = BigDecimal("100000000")
    }

    fun execute(swapErc20ToBep2RequestDto: SwapErc20ToBep2RequestDto): SwapResponeDto {

        val InAmountToRecipient = swapErc20ToBep2RequestDto.erc20OutAmountFromSender

        val swapHistory = SwapHistory()

        swapHistory.erc20SenderAddr = swapErc20ToBep2RequestDto.erc20SenderAddr
        swapHistory.deputyOutAmount = swapErc20ToBep2RequestDto.erc20OutAmountFromSender.toPlainString()
        swapHistory.erc20ChainAddr = DeputyErc20Address
        swapHistory.inAmountToRecipient = InAmountToRecipient.toString()
        swapHistory.outAmountFromSender = swapErc20ToBep2RequestDto.erc20OutAmountFromSender.toPlainString()
        swapHistory.randomNumberHash = swapErc20ToBep2RequestDto.randomNumberHash
        swapHistory.receiverAddr = swapErc20ToBep2RequestDto.bep2RecipientAddr
        swapHistory.senderAddr = DeputyBep2Address
        swapHistory.status = "REQUESTED"
        swapHistory.timestamp = swapErc20ToBep2RequestDto.timestamp
        swapHistory.type = "FromErc20ToBep2"
        swapHistory.expireHeight = 1000

        swapHistory.refundRecipientAddr = swapErc20ToBep2RequestDto.erc20SenderAddr

        swapHistoryRepository!!.save(swapHistory)

        return SwapResponeDto(depositAddress = DeputyErc20Address, amount = InAmountToRecipient)
    }
}
