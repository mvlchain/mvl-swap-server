package io.mvlchain.mvlswap.usecase

import com.binance.dex.api.client.BinanceDexApiClientFactory
import com.binance.dex.api.client.BinanceDexApiNodeClient
import com.binance.dex.api.client.BinanceDexEnvironment
import com.binance.dex.api.client.Wallet
import com.binance.dex.api.client.domain.TransactionMetadata
import com.binance.dex.api.client.domain.broadcast.TransactionOption
import com.binance.dex.api.client.encoding.message.bridge.ClaimTypes
import io.mvlchain.mvlswap.boundary.dto.SwapClaimDto
import io.mvlchain.mvlswap.model.SwapHistory
import io.mvlchain.mvlswap.repository.SwapHistoryRepository
import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.stereotype.Component
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Bytes20
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint64
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.util.*

@Component
class SwapClaimUsecase(private val swapHistoryRepository: SwapHistoryRepository) {

    private val GetProviderDev: String = ETHProvider.getAPINode("ETH")
    private val Erc20PrivateKey = "0x0000000000000000000000000000000000000000"
    private val Bep2PrivateKey = "0x0000000000000000000000000000000000000000"
    private val GasPrice = "60"
    private val GasLimit = 480000L
    private val Erc20SwapContractAddr = "0x0000000000000000000000000000000000000000"

    fun execute(hash: String, swapClaimDto: SwapClaimDto) {

        try {

            val swapHistory: SwapHistory = swapHistoryRepository.findByRandomNumberHash(hash)!!

            //ERC20 claim
            val swapId = swapHistory.erc20ChainSwapId
            val bytes32SwapID = Numeric.hexStringToByteArray(swapId)

            val randomNumber = swapClaimDto.randomNumber
            val bytes32RandomNumber = Numeric.hexStringToByteArray(randomNumber)

            val claimFunction = Function(
                "claim",
                listOf(
                    Bytes32(bytes32SwapID),
                    Bytes32(bytes32RandomNumber)
                ),
                Collections.emptyList()
            )

            val web3 = Web3j.build(HttpService(GetProviderDev))
            val credentials: Credentials = Credentials.create(Erc20PrivateKey)

            val ethGetTransactionCount: EthGetTransactionCount = web3
                .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send()
            val nonce: BigInteger = ethGetTransactionCount.transactionCount

            val gasPrice = Convert.toWei(GasPrice, Convert.Unit.GWEI).toBigInteger()
            val gasLimit = BigInteger.valueOf(GasLimit)

            val rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit,
                Erc20SwapContractAddr, FunctionEncoder.encode(claimFunction)
            )

            val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
            val hexValue = Numeric.toHexString(signedMessage)
            val ethSendTransaction = web3.ethSendRawTransaction(hexValue).send()

            val transactionHash = ethSendTransaction.transactionHash

            var transactionReceipt: Optional<TransactionReceipt?>? = null
            do {
                System.out.println("checking if transaction " + transactionHash.toString() + " is mined....")
                val ethGetTransactionReceiptResp = web3.ethGetTransactionReceipt(transactionHash).send()
                transactionReceipt = ethGetTransactionReceiptResp.transactionReceipt
                Thread.sleep(3000) // Wait 3 sec
            } while (!transactionReceipt!!.isPresent)

            ///////////////////////////////////////////////////////////////////////////////////////
            //<-- binance claim

            val binanceDexApiNodeClient: BinanceDexApiNodeClient = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(
                "http://data-seed-pre-0-s1.binance.org:80",
                BinanceDexEnvironment.TEST_NET.getHrp(),
                BinanceDexEnvironment.TEST_NET.getValHrp()
            )

            val wallet: Wallet = Wallet(Bep2PrivateKey, BinanceDexEnvironment.TEST_NET )

            var listTxMetadata:List<TransactionMetadata> = binanceDexApiNodeClient.claimHtlt(
                swapHistory.bnbChainSwapId, bytes32RandomNumber, wallet, TransactionOption.DEFAULT_INSTANCE, true )

            swapHistory.status = "CLAIMED"

            swapHistoryRepository!!.save(swapHistory)


        } catch (e: Exception) {
            println(
                """
            Exception: ${e.message}
            
            """.trimIndent()
            )
            e.printStackTrace()
        }

    }
}