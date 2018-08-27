package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.response.StatusMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PingController {
    @Authentication
    @GetMapping("/ping")
    fun ping(@RequestAttribute("user") user: String): ResponseEntity<StatusMessage> {
        println(user)
        return ResponseEntity(StatusMessage(null), HttpStatus.OK)
    }
}