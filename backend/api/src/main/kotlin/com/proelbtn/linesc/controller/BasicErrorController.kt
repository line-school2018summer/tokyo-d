package com.proelbtn.linesc.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BasicErrorController : ErrorController {
    @RequestMapping("/error")
    fun error(): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    override fun getErrorPath(): String {
        return "/error"
    }
}