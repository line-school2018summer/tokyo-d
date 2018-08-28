package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.request.CreateUserRequest
import com.proelbtn.linesc.message.response.UserResponse
import com.proelbtn.linesc.model.Users
import com.proelbtn.linesc.validator.validate_id
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
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
    fun createUserInformation(@RequestBody req: CreateUserRequest): ResponseEntity<UserResponse> {
        var message: UserResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validate
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        // operation
        if (status == HttpStatus.OK) {
            val uuid = UUID.randomUUID()
            val now = DateTime.now()
            val hpass = BCrypt.hashpw(req.pass, BCrypt.gensalt(8))

            transaction {
                // check if user is already registered
                val user = Users.select { Users.sid eq req.sid }.firstOrNull()
                if (user != null) status = HttpStatus.BAD_REQUEST

                // if user isn't registered, register it
                if (status == HttpStatus.OK) {
                    Users.insert {
                        it[id] = uuid
                        it[sid] = req.sid
                        it[name] = req.name
                        it[pass] = hpass
                        it[createdAt] = now
                        it[updatedAt] = now
                    }
                }
            }

            if (status == HttpStatus.OK)
                message = UserResponse(uuid.toString(), req.sid, req.name, now.toString(), now.toString())
        }

        return ResponseEntity(message, status)
    }

    @GetMapping(
            "/users/{id}"
    )
    fun getUserInformation(@PathVariable("id") id: String): ResponseEntity<UserResponse> {
        var message: UserResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(id)) status = HttpStatus.BAD_REQUEST

        // operation
        if (status == HttpStatus.OK) {
            val user = transaction { Users.select { Users.id eq UUID.fromString(id) }.firstOrNull() }

            if (user == null) status = HttpStatus.NOT_FOUND
            else {
                message = UserResponse(
                        user[Users.id].toString(),
                        user[Users.sid].toString(),
                        user[Users.name].toString(),
                        user[Users.createdAt].toString(),
                        user[Users.updatedAt].toString()
                )
            }
        }

        return ResponseEntity(message, status)
    }

    @Authentication
    @DeleteMapping(
            "/users/{id}"
    )
    fun deleteUserInformation(@RequestAttribute("user") user: String,
                                    @PathVariable("id") id: String): ResponseEntity<Unit> {
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST
        else if (user != id) status = HttpStatus.BAD_REQUEST

        // operation
        if (status == HttpStatus.OK) {
            val count = transaction { Users.deleteWhere { Users.id eq UUID.fromString(id) } }

            if (count == 0) status = HttpStatus.BAD_REQUEST
        }

        return ResponseEntity(status)
    }
}