package io.mvlchain.mvlswap.util

class ETHProvider {

    companion object {
        fun getAPIHost(network: String?): String {
            when (network) {
                "TESTNET" -> return "https://testnet//"
                "MAINNET" -> return "https://mainnet//"
            }
            return ""
        }
    }
}