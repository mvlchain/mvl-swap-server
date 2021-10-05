package io.mvlchain.mvlswap.usecase

import com.binance.dex.api.client.BinanceDexApiClientFactory
import com.binance.dex.api.client.BinanceDexApiNodeClient
import com.binance.dex.api.client.BinanceDexEnvironment
import com.binance.dex.api.client.Wallet
import com.binance.dex.api.client.domain.TransactionMetadata
import com.binance.dex.api.client.domain.broadcast.HtltReq
import com.binance.dex.api.client.domain.broadcast.TransactionOption
import com.binance.dex.api.client.encoding.Bech32
import com.binance.dex.api.client.encoding.message.Token
import io.mvlchain.mvlswap.boundary.dto.SwapDepositResponseDto
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
import java.math.BigInteger
import java.util.*


@Component
class SwapDepositUsecase(private val swapHistoryRepository: SwapHistoryRepository) {

    private val GetProviderDev: String = ETHProvider.getDeputyErc20Address()
    private val Erc20PrivateKey = "0x0000000000000000000000000000000000000000"
    private val Bep2PrivateKey = "0x0000000000000000000000000000000000000000"
    private val Erc20SwapContractAddr = "0x0000000000000000000000000000000000000000"
    private val DeputyErc20Address:String="0x0000000000000000000000000000000000000000"
    private val GasPrice = "60"
    private val GasLimit = 480000L

    fun execute(hash: String): SwapDepositResponseDto {

            val swapHistory: SwapHistory = swapHistoryRepository.findByRandomNumberHash(hash)!!

            val randomNumberHash = Numeric.hexStringToByteArray(hash)
            val timeStamp = swapHistory.timestamp
            val heightSpan = swapHistory.expireHeight
            val recipientAddr = swapHistory.erc20ChainAddr
            val bep2SenderB32Data: Bech32.Bech32Data? = Bech32.decode( swapHistory.senderAddr)
            val strBep2SenderB32Data:String = swapHistory.senderAddr!!.substring(bep2SenderB32Data!!.hrp.length)
            val bep2SenderAddr = Numeric.hexStringToByteArray(strBep2SenderB32Data)
            val bep2RecipientB32Data: Bech32.Bech32Data? = Bech32.decode( swapHistory.receiverAddr)
            val strBep2RecipientB32Data:String = swapHistory.receiverAddr!!.substring(bep2RecipientB32Data!!.hrp.length)
            val bep2RecipientAddr  = Numeric.hexStringToByteArray(strBep2RecipientB32Data)

        //7. _outAmount
        val outAmount = BigInteger(swapHistory.outAmountFromSender)
        //8. _bep2Amount
        val bep2Amount = BigInteger(swapHistory.inAmountToRecipient)

        //4. _recipientAddr Erc20
        val refundRecipientAddr = swapHistory.refundRecipientAddr

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

            val web3 = Web3j.build(HttpService(GetProviderDev))
            val credentials: Credentials = Credentials.create(Erc20PrivateKey)

            val ethGetTransactionCount: EthGetTransactionCount = web3
                .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send()
            val nonce: BigInteger = ethGetTransactionCount.transactionCount

            val gasPrice = Convert.toWei(GasPrice, Convert.Unit.GWEI).toBigInteger()
            val gasLimit = BigInteger.valueOf(GasLimit)

            val rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit,
                Erc20SwapContractAddr, FunctionEncoder.encode(htltFunction)
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
                DeputyErc20Address, Erc20SwapContractAddr,
                FunctionEncoder.encode(calSwapIDFunction)
            )

            val ethCall = web3!!.ethCall(transaction, DefaultBlockParameterName.LATEST).send()

            val decode = FunctionReturnDecoder.decode(
                ethCall.result,
                calSwapIDFunction.outputParameters
            )

            val erc20SwapID = javax.xml.bind.DatatypeConverter.printHexBinary(decode.get(0).value as ByteArray?)
            println("erc20SwapID: " + erc20SwapID )

            swapHistory.erc20ChainSwapId = erc20SwapID

            val binanceDexApiNodeClient: BinanceDexApiNodeClient = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(
                "http://data-seed-pre-0-s1.binance.org:80",
                BinanceDexEnvironment.TEST_NET.getHrp(),
                BinanceDexEnvironment.TEST_NET.getValHrp()
            )

            var htltReq:HtltReq = HtltReq()
            htltReq.recipient = swapHistory.receiverAddr
            htltReq.recipientOtherChain = swapHistory.erc20ChainAddr
            htltReq.senderOtherChain = swapHistory.erc20SenderAddr
            htltReq.randomNumberHash = randomNumberHash
            htltReq.timestamp = swapHistory.timestamp
            val token:Token = Token("0x0000000000000000000000000000000000000000", swapHistory.inAmountToRecipient!!.toLong() )
            val listToken:List<Token> = listOf(token)
            htltReq.outAmount = listToken
            htltReq.expectedIncome = swapHistory.inAmountToRecipient
            htltReq.heightSpan = swapHistory.expireHeight
            htltReq.isCrossChain = true

            val wallet: Wallet = Wallet(Bep2PrivateKey, BinanceDexEnvironment.TEST_NET )

            var listTxMetadata:List<TransactionMetadata> = binanceDexApiNodeClient.htlt(htltReq,wallet, TransactionOption.DEFAULT_INSTANCE, true );

        val bep2SwapID = listTxMetadata[0].log.split(" ")[3]
        println("bep2SwapID:" + bep2SwapID)

        swapHistory.bnbChainSwapId = bep2SwapID

            swapHistory.status = "DEPOSITED"

            swapHistoryRepository!!.save(swapHistory)

        return SwapDepositResponseDto(
            erc20SwapID = erc20SwapID,
            erc20TxHash = transactionHash,
            bep2SwapID = bep2SwapID,
            bep2TxHash = listTxMetadata[0].hash
            )
    }
}
