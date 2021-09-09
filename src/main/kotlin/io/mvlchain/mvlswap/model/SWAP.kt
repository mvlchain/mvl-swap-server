package io.mvlchain.mvlswap.model

import org.hibernate.Hibernate
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.DynamicInsert
import java.sql.Date
import javax.persistence.*

@Entity
@DynamicInsert
data class SWAP(

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,
    var type: String? = null,
    val bnbChainSwapId: String? = null,
    val erc20ChainSwapId: String? = null,
    var senderAddr: String? = null,
    var receiverAddr: String? = null,
    var erc20ChainAddr: String? = null,
    var inAmount: String? = null,
    var outAmount: String? = null,
    var deputyOutAmount: String? = null,
    var randomNumberHash: String? = null,
    val expireHeight: Long = 0,
    val height: Long = 0,
    var timestamp: Long = 0,
    val randomNumber: String? = null,
    var status: String? = null,
    val createdAt: Date? = null,
    val updateTime: Date? = null,
    var refundAddr: String? = null,
    val erc20SenderAddr: String? = null
    )