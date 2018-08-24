package com.proelbtn.linesc.controller

import com.proelbtn.linesc.message.TokenMessage
import com.proelbtn.linesc.message.UserMessage
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
import java.util.concurrent.ThreadLocalRandom

@RestController
class TokenController {
    @PostMapping("/token")
    fun getToken(@RequestBody msg: UserMessage): ResponseEntity<TokenMessage> {
        println(msg.sid)
        println(msg.pass)

        var message = TokenMessage(null)
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        val rsid = msg.sid
        val rpass = msg.pass

        if (rsid != null && rpass != null) {
            transaction {
                val query = Users.select { (Users.sid eq rsid) }

                if (query.count() == 1) {
                    val user = query.first()
                    if (BCrypt.checkpw(rpass, user[Users.pass])) {
                        val jedis = Jedis("localhost")
                        var token: String

                        do {
                            val array = ByteArray(32)
                            ThreadLocalRandom.current().nextBytes(array)
                            token = array.map { String.format("%02x", it) }.reduce { acc, s -> acc + s }
                        }
                        while (jedis.setnx(token, user[Users.id].toString()) == 0L)
                        jedis.expire(token, 300)

                        message = TokenMessage(token)
                        status = HttpStatus.OK
                    }
                }
                else if (query.count() == 0) status = HttpStatus.BAD_REQUEST
                else status = HttpStatus.INTERNAL_SERVER_ERROR
            }
        }

        return ResponseEntity(message, status)
    }
}