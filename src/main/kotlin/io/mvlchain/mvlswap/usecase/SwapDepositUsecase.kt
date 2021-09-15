package io.mvlchain.mvlswap.usecase

import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.stereotype.Component
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Bytes20
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint64
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.nio.file.FileSystems
import java.util.*
import java.util.Arrays.*


@Component
class SwapDepositUsecase {

    private val GETPROVIDER_DEV: String = ETHProvider.getAPIHost("TESTNET")
    private val ethereumService: EthereumService? = EthereumService()
    private val pwd = "0x0000000000000000000000000000000000000000"

    fun execute(hash: String) {

        try {

            val charset = Charsets.UTF_8
            val byteArray = hash.toByteArray(charset)

            val randomNumberHash = Numeric.hexStringToByteArray(hash)
            val recipientAddr = Numeric.hexStringToByteArray(hash)
            //bep2SenderAddr
            val bep2SenderAddr = Numeric.hexStringToByteArray("0x0000000000000000000000000000000000000000")

            val function = Function(
                "htlt",
                asList(
                    Bytes32(randomNumberHash),
                    Uint64(0),
                    Uint256(1000),
                    Address("0x0000000000000000000000000000000000000000"),
                    Bytes20(bep2SenderAddr),
                    Bytes20(bep2SenderAddr),
                    Uint256(100),
                    Uint256(100)
                ) as List<Type<Bytes32>>?,
                Collections.emptyList()
            )

            val web3 = Web3j.build(HttpService(GETPROVIDER_DEV))
            val credentials: Credentials = Credentials.create(pwd)

            val ethGetTransactionCount: EthGetTransactionCount = web3
                .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send()
            val nonce: BigInteger = ethGetTransactionCount.transactionCount
            println(nonce)

            // 2. sendTransaction
            val txHash: String = ethereumService?.ethSendTransaction(function)!!


        } catch (e: Exception) {
            println(
                """
            Web3j: ${e.message}
            
            """.trimIndent()
            )
            e.printStackTrace()
        }
    }


}
