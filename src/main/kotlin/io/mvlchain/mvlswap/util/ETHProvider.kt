package io.mvlchain.mvlswap.util

class ETHProvider {

    
    enum class DeployPhase {
        develop, production
    }

    enum class APIPhase {
        testnet, mainnet
    }

    var phase = DeployPhase.develop
    var apiPhase = APIPhase.testnet

    private val apiKey = "0x0000000000000000000000000000000000000000"

    fun getAPINode(code: String?): String? {
        return Companion.getAPIHost(code) + apiKey
    }

    fun getAPIKey(): String? {
        return apiKey
    }

    fun getExplorerAddress(code: String?): String? {
        when (code) {
            "ETH" -> return "https://etherscan.io/address/"
        }
        return ""
    }

    fun getExplorerTx(code: String?): String? {
        when (code) {
            "ETH" -> return "https://etherscan.io/tx/"
        }
        return ""
    }

    companion object {
        fun getAPIHost(code: String?): String {
            when (code) {
                "ETH" -> return "0x0000000000000000000000000000000000000000"
            }
            return ""
        }

        fun getAPINode(s: String): String {
            return getAPIHost(s)
        }
    }
}