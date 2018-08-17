package com.proelbtn.linesc.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class PingController {
    @GetMapping(
            value = ["/ping"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun ping(): String {
        return "{\"status\": \"OK\"}"
    }
}