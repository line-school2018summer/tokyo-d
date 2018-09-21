package com.proelbtn.linesc.controller

import com.proelbtn.linesc.exceptions.BadRequestException
import com.proelbtn.linesc.exceptions.ForbiddenException
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
import org.springframework.web.bind.annotation.ResponseStatus
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
            value = "トークン取得用",
            notes = "トークンを取得するために使用するエンドポイント。",
            response = TokenResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にトークンを取得できた。" )),
                (ApiResponse(code = 403, message = "ユーザが存在しないかパスワードが間違っている。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getToken(
            @ApiParam(value = "トークンを取得したいユーザの認証情報") @RequestBody req: GetTokenRequest
                ): TokenResponse {
        // operation
        val sid = req.sid
        val pass = req.pass
        var token = UUID.randomUUID().toString()

        transaction {
            val user = Users.select { Users.sid eq sid }.firstOrNull()

            if (user == null) throw ForbiddenException()
            if (!BCrypt.checkpw(pass, user[Users.pass])) throw ForbiddenException()

            val jedis = Jedis("localhost")
            jedis.set(token, user[Users.id].toString())
            jedis.expire(token, 1800)
        }

        return TokenResponse(token)
    }
}
