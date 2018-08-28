package com.proelbtn.linesc.controller

import com.proelbtn.linesc.message.request.GetTokenRequest
import com.proelbtn.linesc.message.response.TokenResponse
import com.proelbtn.linesc.model.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import redis.clients.jedis.Jedis
import java.util.*

@RestController
class TokenController {
    @PostMapping("/token")
    fun getToken(@RequestBody req: GetTokenRequest): ResponseEntity<TokenResponse> {
        var message = TokenResponse(null)
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        // operation
        if (status == HttpStatus.OK) {
            transaction {
                val user = Users.select { Users.sid eq req.sid }.firstOrNull()

                if (user == null) status = HttpStatus.BAD_REQUEST
                else if (BCrypt.checkpw(req.pass, user[Users.pass])) {
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