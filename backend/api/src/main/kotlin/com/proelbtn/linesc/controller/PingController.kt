package com.proelbtn.linesc.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PingController {
    @GetMapping(
            value = "/ping",
            produces = [ APPLICATION_JSON_VALUE ]
    )
    @ApiOperation(
            value = "サーバの生存確認用",
            notes = "サーバの生存確認やAPIサーバの存在確認に使うかも知れないエンドポイント。",
            response = Unit::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正しくリクエストが受け入れられた。"))
            ]
    )
    fun ping(): ResponseEntity<Unit> {
        return ResponseEntity(Unit, HttpStatus.OK)
    }
}