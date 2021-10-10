package io.mvlchain.mvlswap.usecase

import io.mvlchain.mvlswap.boundary.dto.SwapErc20ToBep2RequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapResponeDto
import io.mvlchain.mvlswap.model.SwapHistory
import io.mvlchain.mvlswap.model.SwapType
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
    }

    fun execute(swapErc20ToBep2RequestDto: SwapErc20ToBep2RequestDto): SwapResponeDto {

        val fee: String = "0"

        val amountToRecipient = swapErc20ToBep2RequestDto.erc20OutAmountFromSender.subtract(BigDecimal(Fee))

        val swap = SwapHistory()

        swap.erc20SenderAddr = swapErc20ToBep2RequestDto.erc20SenderAddr
        // <--V
        swap.deputyOutAmount = swapErc20ToBep2RequestDto.erc20OutAmountFromSender.subtract(BigDecimal(Fee)).toPlainString()
        // <--V
        swap.erc20ChainAddr = DeputyErc20Address
        // <--V
        swap.inAmountToRecipient = amountToRecipient.toString()
        // <--V
        swap.outAmountFromSender = swapErc20ToBep2RequestDto.erc20OutAmountFromSender.toPlainString()
        // <--V
        swap.randomNumberHash = swapErc20ToBep2RequestDto.randomNumberHash
        // <--V
        // Bep2 ReceipientAddr: tbnb15fe7p6tsaf6gghpgdd4tdqm6kwtryd60rnt2s8
        swap.receiverAddr = swapErc20ToBep2RequestDto.bep2RecipientAddr
        // <--V
        swap.senderAddr = DeputyBep2Address
        swap.status = "REQUESTED"
        swap.timestamp = swapErc20ToBep2RequestDto.timestamp
        swap.type = SwapType.TO_BEP2
        swap.expireHeight = 1000

        swap.refundRecipientAddr = swapErc20ToBep2RequestDto.erc20SenderAddr

        swapHistoryRepository.save(swap)

        return SwapResponeDto(depositAddress = DeputyErc20Address, amount = amountToRecipient)
    }
}
