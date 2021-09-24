package io.mvlchain.mvlswap.util

import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal

@Service
class EthereumService(
) {
    private val web3 = Web3j.build(HttpService(""))
    private val appWalletPrivateKey: String = "" // TODO : load from KMS

    fun ethSend(to: String, amount: BigDecimal): TransactionReceipt {
        val credentials = Credentials.create(appWalletPrivateKey)
        val receipt = Transfer.sendFunds(web3, credentials, to, "0.003".toBigDecimal(), Convert.Unit.ETHER).send()

        return receipt
    }
    fun erc20Approve(sourceWalletPkey: String, tokenContractAddress: String, amount: BigDecimal) {
        
    }
}