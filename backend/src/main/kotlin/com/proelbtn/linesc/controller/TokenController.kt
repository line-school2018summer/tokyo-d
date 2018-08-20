package com.proelbtn.linesc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proelbtn.linesc.model.Users
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import redis.clients.jedis.Jedis
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@RestController
class TokenController {
    @PostMapping(
            "/token"
    )
    fun getToken(@RequestBody body: String): ResponseEntity<String> {
        var message: String = "{}"
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        val mapper = ObjectMapper()
        val node = mapper.readTree(body)

        val rsid = node.path("sid").asText()
        val rpass = node.path("pass").asText()

        transaction {
            val query = Users.select { (Users.sid eq rsid) }

            if (query.count() == 1) {
                val user = query.first()
                if (BCrypt.checkpw(rpass, user[Users.pass])) {
                    val jedis = Jedis("localhost")
                    var token: String

                    do {
                        token = ""
                        val array = ByteArray(32)
                        ThreadLocalRandom.current().nextBytes(array)
                        array.forEach { token += String.format("%02x", it) }
                    }
                    while (jedis.setnx(token, user[Users.id].toString()) == 0L)
                    jedis.expire(token, 300)

                    val mapper = ObjectMapper()
                    val node = mapper.createObjectNode()
                    node.put("token", token)

                    message = mapper.writeValueAsString(node)
                    status = HttpStatus.OK
                }
            }
            else if (query.count() == 0) status = HttpStatus.BAD_REQUEST
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }
}