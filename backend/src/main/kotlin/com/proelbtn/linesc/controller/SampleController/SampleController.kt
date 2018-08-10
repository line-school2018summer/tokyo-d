package com.proelbtn.linesc.controller.SampleController

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class SampleController {
    @GetMapping(
            value = ["/"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun hello(): String {
        return "{\"greeting\": \"Hello World!\"}"
    }
}