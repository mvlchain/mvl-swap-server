package io.mvlchain.mvlswap.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
@EntityListeners(AuditingEntityListener::class)
class DepositAddress {
    @Id
    @Column(nullable = false)
    var id: String = ""

    @Column
    var chain: String = "ethereum"

    @Enumerated(EnumType.STRING)
    @Column
    var status: DepositAddressStatus = DepositAddressStatus.READY

    @Column
    var address: String = ""

    @CreatedDate
    @Column(updatable = false)
    var createdAt: Instant? = null

    @LastModifiedDate
    @Column(updatable = true)
    var updatedAt: Instant? = null
}