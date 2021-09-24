package io.mvlchain.mvlswap.usecase

import org.springframework.stereotype.Component
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Component
class PrepareDepositAddressUsecase {
    val web3 = Web3j.build(HttpService(""))

    fun execute() {
        // create new address
        val ecKeyPair = Keys.createEcKeyPair();
        val privKey = ecKeyPair.privateKey
        val address = Keys.getAddress(ecKeyPair)

        // fund eth to this account


        // approve
    }
}