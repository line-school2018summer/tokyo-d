package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authorization
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class PingController {
    @Authorization
    @GetMapping(
            value = ["/ping"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun ping(): String {
        return "{\"status\": \"OK\"}"
    }
}