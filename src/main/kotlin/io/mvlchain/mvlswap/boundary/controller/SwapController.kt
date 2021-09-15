package io.mvlchain.mvlswap.boundary.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mvlchain.mvlswap.boundary.dto.SwapHistoryResponseDto
import io.mvlchain.mvlswap.boundary.dto.SwapRequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapResponeDto
import io.mvlchain.mvlswap.model.SwapStatus
import io.mvlchain.mvlswap.repository.SwapHistoryRepository
import io.mvlchain.mvlswap.usecase.RefundUsecase
import io.mvlchain.mvlswap.usecase.RequestSwapUsecase
import io.mvlchain.mvlswap.usecase.SwapClaimUsecase
import io.mvlchain.mvlswap.usecase.SwapDepositUsecase

import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping(path = ["/swapHistory"])
class SwapController(
    private val swapRepository: SwapHistoryRepository,
    private val swapDepositUsecase: SwapDepositUsecase,
    private val refundUsecase: RefundUsecase,
    private val swapClaimUsecase: SwapClaimUsecase,
    private val requestSwapUsecase: RequestSwapUsecase
) {
    private val GETPROVIDER_DEV: String = ETHProvider.getAPIHost("ETH");

    //SWAP신청

    @PostMapping
    fun requestSwap(
        @RequestBody @Validated swapRequestDto: SwapRequestDto,
    ): SwapResponeDto {
        return requestSwapUsecase.execute(swapRequestDto)
    }

    // Confirm Deposit

    @PostMapping("/{hash}/deposit")
    fun deposit(@PathVariable hash: String) {
        swapDepositUsecase.execute(hash)
    }

    // Hash로 검색하기

    @GetMapping("/{hash}")
    fun findSwapHistoryByHash(
        @PathVariable hash: String
    ): SwapHistoryResponseDto? {


        val swapHistory = swapRepository.findByRandomNumberHash(hash) ?: throw Exception("not found")

        val swapHistoryResponseDto = SwapHistoryResponseDto(hash = hash, type = swapHistory.type!!, status = SwapStatus.valueOf(swapHistory.status.toString()))
        return swapHistoryResponseDto
    }


    // sender로 검색하기

    //swapHistory/{hash}
    @GetMapping("/Sender/{sender}")
    fun findSwapHistoryBySender(
        @PathVariable sender: String
    ): SwapHistoryResponseDto {

        val swapHistory = swapRepository.findByErc20SenderAddr(sender) ?: throw Exception("not found")
        TODO("implement")

        val swapHR: SwapHistoryResponseDto

        return swapHR
    }

    // Confirm Deposit

    @PostMapping("/{hash}/claim")
    fun claim(@PathVariable hash: String) {
        swapClaimUsecase.execute(hash)
    }

    @PostMapping("/{hash}/refund")
    fun refund(@PathVariable hash: String) {
        refundUsecase.execute(hash)
    }
}