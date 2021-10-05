package io.mvlchain.mvlswap.util

class ETHProvider {

    companion object {
        private val Erc20PrivateKey = "0x0000000000000000000000000000000000000000"
        private val Erc20Provider = "0x0000000000000000000000000000000000000000/"

        private val DeputyErc20Address:String="0x0000000000000000000000000000000000000000"
        private val DeputyBep2Address:String="0x0000000000000000000000000000000000000000"

        private val GasPrice = "60"
        private val GasLimit = 480000L

        private val Erc20SwapContractAddr = "0x0000000000000000000000000000000000000000"

        fun getErc20Provider(): String {
            return Erc20Provider
        }

        fun getErc20PrivateKey(): String {
            return Erc20PrivateKey
        }

        fun getDeputyErc20Address(): String {
            return DeputyErc20Address
        }

        fun getDeputyBep2Address(): String {
            return DeputyBep2Address
        }

        fun getGasPrice(): String {
            return GasPrice
        }

        fun getGasLimit(): Long {
            return GasLimit
        }

        fun getErc20SwapContractAddr(): String {
            return Erc20SwapContractAddr
        }

    }
}