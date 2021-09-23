package io.mvlchain.mvlswap.usecase

import io.mvlchain.mvlswap.boundary.dto.SwapBep2ToErc20RequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapResponeDto
import io.mvlchain.mvlswap.model.SwapHistory
import io.mvlchain.mvlswap.repository.SwapHistoryRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class RequestBep2ToErc20SwapUsecase(

    private val swapHistoryRepository: SwapHistoryRepository
) {


    companion object {
        private val Fee:String="0"
        private val DeputyErc20Address:String="0xd2E5C581A406F28a3DC8Ab0313B68950B58dd4AD"
        private val DeputyBep2Address:String="tbnb15fe7p6tsaf6gghpgdd4tdqm6kwtryd60rnt2s8"

    }

    fun execute(swapBep2ToErc20RequestDto: SwapBep2ToErc20RequestDto): SwapResponeDto {

        val fee:String = "0"

        //<--V FEE 만큼 수수료 임시로 책정정
        val InAmountToRecipient = swapBep2ToErc20RequestDto.bep2OutAmount!!.subtract(BigDecimal(Fee))
        //<--V
        val swap = SwapHistory()

        //<-- erc20SenderAddr 는 없으므로
        //swap.erc20SenderAddr = swapRequestDto.erc20SenderAddr
        swap.erc20SenderAddr = DeputyErc20Address
        //deputyOutAmount 이건.. Erc20 MVL 갯수얌
        swap.deputyOutAmount = swapBep2ToErc20RequestDto.bep2OutAmount!!.subtract(BigDecimal(Fee)).toPlainString()
        //<-- 이건 받는 사람 계좌
        //swap.erc20ChainAddr = DeputyErc20Address
        swap.erc20ChainAddr = swapBep2ToErc20RequestDto.erc20RecipientAddr

        swap.inAmountToRecipient = InAmountToRecipient.toString()

        swap.outAmountFromSender = swapBep2ToErc20RequestDto.bep2OutAmount!!.toPlainString()

        swap.randomNumberHash = swapBep2ToErc20RequestDto.randomNumberHash

        swap.receiverAddr = DeputyBep2Address

        swap.senderAddr = swapBep2ToErc20RequestDto.bep2SenderAddr
        swap.status = "REQUESTED"
        swap.timestamp = swapBep2ToErc20RequestDto.timestamp
        swap.type = "FromBep2ToErc20"
        swap.expireHeight = 1000

        swapHistoryRepository!!.save(swap)

        return SwapResponeDto(depositAddress = DeputyBep2Address, amount = InAmountToRecipient)
    }
}



