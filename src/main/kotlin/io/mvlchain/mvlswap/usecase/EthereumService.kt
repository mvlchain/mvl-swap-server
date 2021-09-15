package io.mvlchain.mvlswap.usecase

import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.stereotype.Component
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.datatypes.Function
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.nio.file.FileSystems
import java.util.concurrent.ExecutionException


@Component
class EthereumService {

    private val from = "0x0000000000000000000000000000000000000000"

    private val contract = "0x0000000000000000000000000000000000000000"

    // hardcording because of testing
    private val pwd = "0x0000000000000000000000000000000000000000"

    private var web3j: Admin? = null

    private val GETPROVIDER_DEV: String = ETHProvider.getAPIHost("TESTNET")

    fun EthereumService() {

        web3j = Admin.build(HttpService(GETPROVIDER_DEV))
    }

    @Throws(IOException::class)
    fun ethCall(function: Function): Any? {

        // 1. Account Lock 해제
        val personalUnlockAccount = web3j!!.personalUnlockAccount(from, pwd).send()

        return if (personalUnlockAccount.accountUnlocked()) { // unlock 일때

            //2. transaction 제작
            val transaction = Transaction.createEthCallTransaction(
                from, contract,
                FunctionEncoder.encode(function)
            )

            //3. ethereum 호출후 결과 가져오기
            val ethCall = web3j!!.ethCall(transaction, DefaultBlockParameterName.LATEST).send()

            //4. 결과값 decode
            val decode = FunctionReturnDecoder.decode(
                ethCall.result,
                function.outputParameters
            )
            println("ethCall.getResult() = " + ethCall.result)
            println("getValue = " + decode[0].value)
            println("getType = " + decode[0].typeAsString)
            decode[0].value
        } else {
            throw PersonalLockException("check ethereum personal Lock")
        }
    }

    @Throws(IOException::class, InterruptedException::class, ExecutionException::class)
    fun ethSendTransaction(function: Function?): String? {
        return null
    }

    @Throws(IOException::class)
    fun getReceipt(transactionHash: String?): TransactionReceipt? {
        val transactionReceipt: EthGetTransactionReceipt = web3j?.ethGetTransactionReceipt(transactionHash)!!.send()
        if (transactionReceipt.transactionReceipt.isPresent) {
            println(
                "transactionReceipt.getResult().getContractAddress() = " +
                        transactionReceipt.result
            )
        } else {
            println("transaction complete not yet")
        }
        return transactionReceipt.result
    }

    private class PersonalLockException(msg: String?) : RuntimeException(msg)
}