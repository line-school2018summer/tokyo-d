package com.proelbtn.linesc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proelbtn.linesc.model.Users
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
    fun createUserInformation(@RequestBody body: String): ResponseEntity<String> {
        var message: String = "{}"
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        val mapper = ObjectMapper()
        val node = mapper.readTree(body)

        val rsid = node.path("sid").asText()
        val rname = node.path("name").asText()
        val rpass = node.path("pass").asText()

        if (rsid.isNullOrEmpty() || rname.isNullOrEmpty() || rpass.isNullOrEmpty());

        transaction {
            val query = Users.select { Users.sid eq rsid }

            if (query.count() == 0) {
                val now = DateTime.now()
                Users.insert {
                    it[id] = UUID.randomUUID()
                    it[sid] = rsid
                    it[name] = rname
                    it[pass] = BCrypt.hashpw(rpass, BCrypt.gensalt(8))
                    it[createdAt] = now
                    it[updatedAt] = now
                }

                val query = Users.select { Users.sid eq rsid }

                if (query.count() == 1) {
                    val user = query.first()
                    val mapper = ObjectMapper()
                    val node = mapper.createObjectNode()
                    node.put("id", user[Users.id].toString())
                    node.put("sid", user[Users.sid].toString())
                    node.put("name", user[Users.name].toString())
                    node.put("created_at", user[Users.createdAt].toString())
                    node.put("updated_at", user[Users.updatedAt].toString())

                    message = mapper.writeValueAsString(node)
                    status = HttpStatus.OK
                }
                else status = HttpStatus.INTERNAL_SERVER_ERROR
            }
            else status = HttpStatus.BAD_REQUEST
        }

        return ResponseEntity(message, status);
    }

    @GetMapping(
            "/users/id/{id}"
    )
    fun getUserInformationFromId(@PathVariable("id") id: String): ResponseEntity<String> {
        var message: String = "{}"
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val query = Users.select { Users.id eq UUID.fromString(id) }

            if (query.count() == 1) {
                val user = query.first()
                val mapper = ObjectMapper()
                val node = mapper.createObjectNode()
                node.put("id", user[Users.id].toString())
                node.put("sid", user[Users.sid].toString())
                node.put("name", user[Users.name].toString())
                node.put("created_at", user[Users.createdAt].toString())
                node.put("updated_at", user[Users.updatedAt].toString())

                message = mapper.writeValueAsString(node)
                status = HttpStatus.OK
            }
            else if (query.count() == 0) status = HttpStatus.NOT_FOUND
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }

    @DeleteMapping(
            "/users/id/{id}"
    )
    fun deleteUserInformationFromId(@PathVariable("id") id: String): ResponseEntity<String> {
        var message: String = "{}"
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val count = Users.deleteWhere { Users.id eq UUID.fromString(id) }

            if (count == 1) status = HttpStatus.OK
            else if (count == 0) status = HttpStatus.BAD_REQUEST
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }

    @GetMapping(
            "/users/sid/{sid}"
    )
    fun getUserInformationFromSid(@PathVariable("sid") sid: String): ResponseEntity<String> {
        var message: String = "{}"
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val query = Users.select { Users.sid eq sid }

            if (query.count() == 1) {
                val user = query.first()
                val mapper = ObjectMapper()
                val node = mapper.createObjectNode()
                node.put("id", user[Users.id].toString())
                node.put("sid", user[Users.sid].toString())
                node.put("name", user[Users.name].toString())
                node.put("created_at", user[Users.createdAt].toString())
                node.put("updated_at", user[Users.updatedAt].toString())

                message = mapper.writeValueAsString(node)
                status = HttpStatus.OK
            }
            else if (query.count() == 0) status = HttpStatus.NOT_FOUND
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }

    @DeleteMapping(
            "/users/sid/{sid}"
    )
    fun deleteUserInformationFromSid(@PathVariable("sid") sid: String): ResponseEntity<String> {
        var message: String = "{}"
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val count = Users.deleteWhere { Users.sid eq sid }

            if (count == 1) status = HttpStatus.OK
            else if (count == 0) status = HttpStatus.BAD_REQUEST
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }
}