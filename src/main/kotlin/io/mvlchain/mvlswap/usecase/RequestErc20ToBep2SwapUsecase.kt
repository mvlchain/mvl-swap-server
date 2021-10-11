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

        val fee: String = "0"

        //<-- fix-decimal Bep2
        val InAmountToRecipient = swapErc20ToBep2RequestDto.erc20OutAmountFromSender!!.subtract(BigDecimal(Fee)).multiply(Bep2BacicUnit)

        val swap = SwapHistory()

        swap.erc20SenderAddr = swapErc20ToBep2RequestDto.erc20SenderAddr
        //<-- fix-decimal Bep2
        swap.deputyOutAmount = swapErc20ToBep2RequestDto.erc20OutAmountFromSender!!.subtract(BigDecimal(Fee)).multiply(Bep2BacicUnit).toPlainString()
        // <--V
        swap.erc20ChainAddr = DeputyErc20Address
        // <--V
        swap.inAmountToRecipient = InAmountToRecipient.toString()
        //<-- fix-decimal Erc20
        swap.outAmountFromSender = swapErc20ToBep2RequestDto.erc20OutAmountFromSender!!.multiply(Erc20BasicUnit).toPlainString()
        // <--V
        swap.randomNumberHash = swapErc20ToBep2RequestDto.randomNumberHash
        // <--V
        // Bep2 ReceipientAddr: tbnb15fe7p6tsaf6gghpgdd4tdqm6kwtryd60rnt2s8
        swap.receiverAddr = swapErc20ToBep2RequestDto.bep2RecipientAddr
        // <--V
        swap.senderAddr = DeputyBep2Address
        swap.status = "REQUESTED"
        swap.timestamp = swapErc20ToBep2RequestDto.timestamp
        swap.type = "FromErc20ToBep2"
        swap.expireHeight = 1000

        swap.refundRecipientAddr = swapErc20ToBep2RequestDto.erc20SenderAddr

        swapHistoryRepository!!.save(swap)

        return SwapResponeDto(depositAddress = DeputyErc20Address, amount = InAmountToRecipient)
    }
}
