package io.mvlchain.mvlswap.model

import org.hibernate.annotations.DynamicInsert
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@DynamicInsert
@EntityListeners(AuditingEntityListener::class)
class SwapHistory {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Enumerated(EnumType.STRING)
    var type: SwapType = SwapType.TO_BEP2
    var bnbChainSwapId: String? = null
    var erc20ChainSwapId: String? = null
    var senderAddr: String? = null
    var receiverAddr: String? = null
    var erc20ChainAddr: String? = null
    var inAmountToRecipient: String? = null
    var outAmountFromSender: String? = null
    var deputyOutAmount: String? = null
    var randomNumberHash: String? = null
    var expireHeight: Long = 0
    val height: Long = 0
    var timestamp: Long = 0
    var randomNumber: String? = null
    var status: String? = null
    var erc20SenderAddr: String? = null
    var refundRecipientAddr: String? = null

    @CreatedDate
    @Column(updatable = false)
    var createdAt: Instant? = null

    @LastModifiedDate
    @Column(updatable = true)
    var updatedAt: Instant? = null
}
