package io.mvlchain.mvlswap.boundary.controller

import io.mvlchain.mvlswap.boundary.dto.SwapAddressResponseDto
import io.mvlchain.mvlswap.boundary.dto.SwapBep2ToErc20RequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapBep2ToErc20ResponseDto
import io.mvlchain.mvlswap.boundary.dto.SwapClaimDto
import io.mvlchain.mvlswap.boundary.dto.SwapDepositResponseDto
import io.mvlchain.mvlswap.boundary.dto.SwapErc20ToBep2RequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapHistoryResponseDto
import io.mvlchain.mvlswap.boundary.dto.SwapResponeDto
import io.mvlchain.mvlswap.model.SwapStatus
import io.mvlchain.mvlswap.repository.SwapHistoryRepository
import io.mvlchain.mvlswap.usecase.RefundUsecase
import io.mvlchain.mvlswap.usecase.RequestBep2ToErc20SwapUsecase
import io.mvlchain.mvlswap.usecase.RequestErc20ToBep2SwapUsecase
import io.mvlchain.mvlswap.usecase.SwapClaimUsecase
import io.mvlchain.mvlswap.usecase.SwapDepositUsecase
import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/swapHistory"])
class SwapController(
    private val swapRepository: SwapHistoryRepository,
    private val swapDepositUsecase: SwapDepositUsecase,
    private val refundUsecase: RefundUsecase,
    private val swapClaimUsecase: SwapClaimUsecase,
    private val requestErc20ToBep2SwapUsecase: RequestErc20ToBep2SwapUsecase,
    private val requestBep2ToErc20SwapUsecase: RequestBep2ToErc20SwapUsecase
) {
    private val GETPROVIDER_DEV: String = ETHProvider.getErc20Provider()
    private val Erc20SwapDepositAddress: String = "0x000000"
    private val Bep2DepositAddress: String = "tbnb0000000"

    @PostMapping("/erc20")
    fun requestSwap(
        @RequestBody @Validated swapErc20ToBep2RequestDto: SwapErc20ToBep2RequestDto,
    ): SwapResponeDto {
        return requestErc20ToBep2SwapUsecase.execute(swapErc20ToBep2RequestDto)
    }

    @PostMapping("/bep2")
    fun requestBep2ToErc20Swap(
        @RequestBody @Validated swapBep2ToErc20RequestDto: SwapBep2ToErc20RequestDto,
    ): SwapBep2ToErc20ResponseDto {
        return requestBep2ToErc20SwapUsecase.execute(swapBep2ToErc20RequestDto)
    }

    @PostMapping("/{hash}/deposit")
    fun deposit(
        @PathVariable hash: String
    ): SwapDepositResponseDto {
        return swapDepositUsecase.execute(hash)
    }

    @PostMapping("/{hash}/claim")
    fun claim(@PathVariable hash: String, @RequestBody @Validated swapClaimDto: SwapClaimDto) {
        swapClaimUsecase.execute(hash, swapClaimDto)
    }

    @PostMapping("/{hash}/refund")
    fun refund(@PathVariable hash: String) {
        refundUsecase.execute(hash)
    }

    @GetMapping("/{hash}")
    fun findSwapHistoryByHash(
        @PathVariable hash: String
    ): SwapHistoryResponseDto {
        val swapHistory = swapRepository.findByRandomNumberHash(hash) ?: throw Exception("not found")
        return SwapHistoryResponseDto(hash = hash, type = swapHistory.type!!, status = SwapStatus.valueOf(swapHistory.status.toString()))
    }

    @GetMapping("/depositAddress")
    fun getSwapAddress(): SwapAddressResponseDto {
        return SwapAddressResponseDto(Erc20SwapDepositAddress = Erc20SwapDepositAddress, Bep2DepositAddress = Bep2DepositAddress)
    }

    // swapHistory/{hash}
    @GetMapping("/Sender/{sender}")
    fun findSwapHistoryBySender(
        @PathVariable sender: String
    ): SwapHistoryResponseDto {
        val swapHistory = swapRepository.findByErc20SenderAddr(sender) ?: throw Exception("not found")
        TODO("implement")
    }
}
