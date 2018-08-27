package com.proelbtn.linesc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proelbtn.linesc.message.request.UserSelector
import com.proelbtn.linesc.message.response.UserResponseMessage
import com.proelbtn.linesc.model.Users
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UsersController {
    @PostMapping(
            "/users"
    )
    fun createUserInformation(@RequestBody selector: UserSelector): ResponseEntity<UserResponseMessage> {
        var message: UserResponseMessage? = null
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        val rsid = selector.sid
        val rname = selector.name
        val rpass = selector.pass

        if (rsid == null || rname == null || rpass == null) status = HttpStatus.BAD_REQUEST
        else if (rsid.isEmpty() || rname.isEmpty() || rpass.isEmpty()) status = HttpStatus.BAD_REQUEST
        else {
            transaction {
                val query = Users.select { Users.sid eq rsid }.firstOrNull()

                if (query != null) status = HttpStatus.BAD_REQUEST
                else {
                    val now = DateTime.now()
                    val uuid = UUID.randomUUID()

                    Users.insert {
                        it[id] = uuid
                        it[sid] = rsid
                        it[name] = rname
                        it[pass] = BCrypt.hashpw(rpass, BCrypt.gensalt(8))
                        it[createdAt] = now
                        it[updatedAt] = now
                    }

                    val user = Users.select { Users.id eq uuid }.firstOrNull()

                    if (user != null) {
                        message = UserResponseMessage(
                                user[Users.id].toString(),
                                user[Users.sid].toString(),
                                user[Users.name].toString(),
                                user[Users.createdAt].toString(),
                                user[Users.updatedAt].toString()
                        )
                        status = HttpStatus.OK
                    }
                }
            }
        }

        return ResponseEntity(message, status)
    }

    @GetMapping(
            "/users/{id}"
    )
    fun getUserInformationFromId(@PathVariable("id") id: String): ResponseEntity<UserResponseMessage> {
        var message: UserResponseMessage? = null
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val query = Users.select { Users.id eq UUID.fromString(id) }

            if (query.count() == 1) {
                val user = query.first()

                message = UserResponseMessage(
                        user[Users.id].toString(),
                        user[Users.sid].toString(),
                        user[Users.name].toString(),
                        user[Users.createdAt].toString(),
                        user[Users.updatedAt].toString()
                )
                status = HttpStatus.OK
            }
            else if (query.count() == 0) status = HttpStatus.NOT_FOUND
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }

    @DeleteMapping(
            "/users/{id}"
    )
    fun deleteUserInformationFromId(@PathVariable("id") id: String): ResponseEntity<Unit> {
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val count = Users.deleteWhere { Users.id eq UUID.fromString(id) }

            if (count == 1) status = HttpStatus.OK
            else if (count == 0) status = HttpStatus.BAD_REQUEST
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(status)
    }
}