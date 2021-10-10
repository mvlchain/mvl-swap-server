package io.mvlchain.mvlswap.usecase

import io.mvlchain.mvlswap.util.ETHProvider
import org.springframework.stereotype.Component

import org.web3j.abi.datatypes.Function

import org.web3j.protocol.admin.Admin

import org.web3j.protocol.http.HttpService

import java.io.IOException



@Component
class EthereumService {

    private val from = "0x0000000000000000000000000000000000000000"

    private val contract = "0x0000000000000000000000000000000000000000"

    // hardcording because of testing
    private val pwd = "0x0000000000000000000000000000000000000000"

    private var web3j: Admin? = null
    private val GETPROVIDER_DEV: String = ETHProvider.getErc20Provider()

    fun EthereumService() {

        web3j = Admin.build(HttpService(GETPROVIDER_DEV))
    }

    @Throws(IOException::class)
    fun ethCall(function: Function) {

    }

}