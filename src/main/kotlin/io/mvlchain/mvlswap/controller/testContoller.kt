package io.mvlchain.mvlswap.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class testContoller {
    @RequestMapping("/")
    fun index(): String? {
        return "Greetings from MVL SWAP! Kotlin Code is working"
    }
}