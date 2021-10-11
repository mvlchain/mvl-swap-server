package io.mvlchain.mvlswap.usecase

import com.binance.dex.api.client.BinanceDexApiClientFactory
import com.binance.dex.api.client.BinanceDexApiNodeClient
import com.binance.dex.api.client.BinanceDexEnvironment
import com.binance.dex.api.client.encoding.Bech32
import io.mvlchain.mvlswap.boundary.dto.SwapBep2ToErc20RequestDto
import io.mvlchain.mvlswap.boundary.dto.SwapBep2ToErc20ResponseDto
import io.mvlchain.mvlswap.model.SwapHistory
import io.mvlchain.mvlswap.repository.SwapHistoryRepository
import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.stereotype.Component
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
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
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Collections
import java.util.Optional

@Component
class RequestBep2ToErc20SwapUsecase(

    private val swapHistoryRepository: SwapHistoryRepository
) {

    private val Erc20BasicUnitBigDecimal = BigDecimal("1000000000000000000")
    private val Erc20BasicUnitBigInteger = BigInteger("1000000000000000000")
    private val Bep2BacicUnitBigDecimal = BigDecimal("100000000")
    private val Bep2BacicUnitInt = 100000000

    fun execute(swapBep2ToErc20RequestDto: SwapBep2ToErc20RequestDto): SwapBep2ToErc20ResponseDto {

        val binanceDexApiNodeClient: BinanceDexApiNodeClient = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(
            "http://data-seed-pre-1-s3.binance.org:80",
            BinanceDexEnvironment.TEST_NET.getHrp(),
            BinanceDexEnvironment.TEST_NET.getValHrp()
        )

        val atomicSwap = binanceDexApiNodeClient.getSwapByID(swapBep2ToErc20RequestDto.bep2SwapID)


        val outAmountFromSender = atomicSwap.outAmount.get(0).amount

        val swap = SwapHistory()
        swap.erc20SenderAddr = ETHProvider.getDeputyErc20Address()
        //Bep2에서 나가는 갯수 * 기본단위 1e*8
        swap.deputyOutAmount = BigDecimal(outAmountFromSender).divide(Bep2BacicUnitBigDecimal).toPlainString()
        swap.erc20ChainAddr = swap.erc20ChainAddr
        swap.inAmountToRecipient = BigDecimal(outAmountFromSender).divide(Bep2BacicUnitBigDecimal).toPlainString()
        swap.outAmountFromSender = atomicSwap.outAmount.get(0).amount.div(Bep2BacicUnitInt).toString()
        swap.randomNumberHash = atomicSwap.randomNumberHash
        swap.receiverAddr = ETHProvider.getDeputyBep2Address()
        swap.senderAddr = atomicSwap.from
        swap.status = "REQUESTED"
        swap.timestamp = atomicSwap.timestamp
        swap.type = "FromBep2ToErc20"
        swap.expireHeight = 1000

        swapHistoryRepository!!.save(swap)

        // //////////////////////////////////////////////////
        // <-- Regist htlt to Ethereum

        val hash = atomicSwap.randomNumberHash

        val swapHistory: SwapHistory = swapHistoryRepository.findByRandomNumberHash(hash)!!

        // ERC20 htlt
        // 1. _randomNumberHash
        val randomNumberHash = Numeric.hexStringToByteArray(hash)
        // 2. _timestamp
        val timeStamp = swapHistory.timestamp
        // 3. _heightSpan
        val heightSpan = swapHistory.expireHeight
        // 4. _recipientAddr Erc20
        val recipientAddr = swapHistory.erc20ChainAddr

        // 5. _bep2SenderAddr
        val bep2SenderB32Data: Bech32.Bech32Data? = Bech32.decode(swapHistory.senderAddr)
        val strBep2SenderB32Data: String = swapHistory.senderAddr!!.substring(bep2SenderB32Data!!.hrp.length)
        val bep2SenderAddr = Numeric.hexStringToByteArray(strBep2SenderB32Data)

        // 6. _bep2RecipientAddr
        val bep2RecipientB32Data: Bech32.Bech32Data? = Bech32.decode(swapHistory.receiverAddr)
        val strBep2RecipientB32Data: String = swapHistory.receiverAddr!!.substring(bep2RecipientB32Data!!.hrp.length)
        val bep2RecipientAddr = Numeric.hexStringToByteArray(strBep2RecipientB32Data)

        // 7. _outAmount
        val outAmount = BigInteger(swapHistory.outAmountFromSender).multiply(Erc20BasicUnitBigInteger)
        // 8. _bep2Amount
        val bep2Amount = BigInteger(swapHistory.inAmountToRecipient).multiply(Erc20BasicUnitBigInteger)

        // 9. _recipientAddr Erc20
        val refundRecipientAddr = ETHProvider.getDeputyErc20Address()

        val htltFunction = Function(
            "htlt",
            listOf(
                Bytes32(randomNumberHash),
                Uint64(timeStamp),
                Uint256(heightSpan),
                Address(recipientAddr),
                Bytes20(bep2SenderAddr),
                Bytes20(bep2RecipientAddr),
                Uint256(outAmount),
                Uint256(bep2Amount),
                Address(refundRecipientAddr)
            ),
            Collections.emptyList()
        )

        val web3 = Web3j.build(HttpService(ETHProvider.getErc20Provider()))
        val credentials: Credentials = Credentials.create(ETHProvider.getErc20PrivateKey())

        val ethGetTransactionCount: EthGetTransactionCount = web3
            .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send()
        val nonce: BigInteger = ethGetTransactionCount.transactionCount

        val gasPrice = Convert.toWei(ETHProvider.getGasPrice(), Convert.Unit.GWEI).toBigInteger()
        val gasLimit = BigInteger.valueOf(ETHProvider.getGasLimit())

        val rawTransaction = RawTransaction.createTransaction(
            nonce, gasPrice, gasLimit,
            ETHProvider.getErc20SwapContractAddr(), FunctionEncoder.encode(htltFunction)
        )

        val signedMessage = TransactionEncoder.signMessage(rawTransaction, 3.toByte(), credentials)
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

        // /////////////////////////////////////////////////////////////////////////////////////
        // <-- read SwapId

        val typeReference_0: TypeReference<Bytes32?> = object : TypeReference<Bytes32?>() {}
        val references: List<TypeReference<*>> = listOf(typeReference_0)

        val calSwapIDFunction = Function(
            "calSwapID",
            listOf(
                Bytes32(randomNumberHash),
                Address(swapHistory.erc20SenderAddr),
                Bytes20(bep2SenderAddr)
            ),
            references
        )

        val transaction = Transaction.createEthCallTransaction(
            ETHProvider.getDeputyErc20Address(), ETHProvider.getErc20SwapContractAddr(),
            FunctionEncoder.encode(calSwapIDFunction)
        )

        val ethCall = web3!!.ethCall(transaction, DefaultBlockParameterName.LATEST).send()

        val decode = FunctionReturnDecoder.decode(
            ethCall.result,
            calSwapIDFunction.outputParameters
        )

        val erc20SwapID = javax.xml.bind.DatatypeConverter.printHexBinary(decode.get(0).value as ByteArray?)
        println("erc20SwapID: " + erc20SwapID)

        swapHistory.erc20ChainSwapId = erc20SwapID

        return SwapBep2ToErc20ResponseDto(
            erc20SwapID = erc20SwapID,
            erc20TxHash = transactionHash
        )
    }
}
