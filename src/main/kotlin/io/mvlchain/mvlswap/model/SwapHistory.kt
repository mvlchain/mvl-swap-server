package io.mvlchain.mvlswap.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.sql.Date
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@Entity
@EntityListeners(AuditingEntityListener::class)
class SwapHistory {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column
    @Enumerated(EnumType.STRING)
    var type: SwapType = SwapType.TO_BEP2

    @Column
    var bnbChainSwapId: String? = null

    @Column
    var erc20ChainSwapId: String? = null

    @Column
    var senderAddr: String? = null

    @Column
    var receiverAddr: String? = null

    @Column
    var erc20ChainAddr: String? = null

    @Column
    @Digits(integer = 10, fraction = 8)
    var inAmountToRecipient: BigDecimal? = null

    @Column
    @Digits(integer = 10, fraction = 8)
    var outAmountFromSender: BigDecimal? = null

    @Column
    @Digits(integer = 10, fraction = 8)
    var deputyOutAmount: BigDecimal? = null

    @Column
    var randomNumberHash: String? = null

    @Column
    var expireHeight: Long = 0

    @NotNull
    @Column
    var height: Long = 0

    @NotNull
    @Column
    var timestamp: Long = 0

    @NotNull
    @Column
    var status: SwapStatus = SwapStatus.REQUESTED

    var erc20SenderAddr: String? = null

    @CreatedDate
    @Column(updatable = false)
    var createdAt: Instant? = null

    @LastModifiedDate
    @Column(updatable = true)
    var updatedAt: Instant? = null
}
