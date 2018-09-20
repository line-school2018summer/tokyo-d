package com.proelbtn.linesc.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class PingController {
    @GetMapping(
            value = "/ping"
    )
    @ApiOperation(
            value = "サーバの生存確認用",
            notes = "サーバの生存確認やAPIサーバの存在確認に使うかも知れないエンドポイント。"
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正しくリクエストが受け入れられた。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun ping() {}
}