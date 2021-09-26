package io.mvlchain.mvlswap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
class MvlSwapApplication

fun main(args: Array<String>) {
    runApplication<MvlSwapApplication>(*args)
}
