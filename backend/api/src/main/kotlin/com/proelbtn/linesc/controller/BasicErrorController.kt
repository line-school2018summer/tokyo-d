package com.proelbtn.linesc.controller

import com.proelbtn.linesc.exceptions.BadRequestException
import com.proelbtn.linesc.exceptions.ForbiddenException
import com.proelbtn.linesc.exceptions.NotFoundException
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BasicErrorController : ErrorController {
    @RequestMapping("/error")
    fun error(): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException::class)
    fun httpMessageConversion() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class)
    fun badRequest() {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun notFound() {}

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException::class)
    fun forbidden() {}

    override fun getErrorPath(): String {
        return "/error"
    }
}