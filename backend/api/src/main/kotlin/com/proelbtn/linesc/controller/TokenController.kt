package com.proelbtn.linesc.controller

import com.proelbtn.linesc.request.GetTokenRequest
import com.proelbtn.linesc.response.TokenResponse
import com.proelbtn.linesc.model.Users
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import redis.clients.jedis.Jedis
import java.util.*

@RestController
class TokenController {
    @PostMapping(
            value = "/token",
            produces = [ APPLICATION_JSON_VALUE ]
    )
    @ApiOperation(
            value = "トークンの取得用",
            notes = "トークンを取得するために使用するエンドポイント。",
            response = TokenResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にトークンを取得できた。" )),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun getToken(
            @ApiParam(value = "トークンを取得したいユーザの認証情報") @RequestBody req: GetTokenRequest
                ): ResponseEntity<TokenResponse> {
        var message: TokenResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        // operation
        if (status == HttpStatus.OK) {
            val sid = req.sid!!
            val pass = req.pass!!

            transaction {
                val user = Users.select { Users.sid eq sid }.firstOrNull()

                if (user == null) status = HttpStatus.BAD_REQUEST
                else if (BCrypt.checkpw(pass, user[Users.pass])) {
                    val jedis = Jedis("localhost")
                    var token = UUID.randomUUID().toString()

                    jedis.set(token, user[Users.id].toString())
                    jedis.expire(token, 300)

                    message = TokenResponse(token)
                }
            }
        }

        return ResponseEntity(message, status)
    }
}