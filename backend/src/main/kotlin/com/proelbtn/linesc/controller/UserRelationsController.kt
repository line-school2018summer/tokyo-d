package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.request.CreateRelationRequest
import com.proelbtn.linesc.model.UserRelations
import com.proelbtn.linesc.model.Users
import com.proelbtn.linesc.validator.validate_id
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserRelationsController {
    @Authentication
    @PostMapping(
            "/relations/users"
    )
    fun createUserRelation(@RequestAttribute("user") user: String,
                            @RequestBody req: CreateRelationRequest): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(req.from)
            val tid = UUID.fromString(req.to)

            transaction {
                if (status == HttpStatus.OK) {
                    val query = UserRelations.select {
                        (UserRelations.from eq fid) and (UserRelations.to eq tid)
                    }.firstOrNull()

                    if (query != null) status = HttpStatus.BAD_REQUEST
                }

                if (status == HttpStatus.OK) {
                    UserRelations.insert {
                        it[from] = fid
                        it[to] = tid
                        it[createdAt] = DateTime.now()
                    }
                }
            }

        }

        return ResponseEntity(status)
    }

    @Authentication
    @GetMapping(
            "/relations/users/{id}"
    )
    fun getUserRelation(@RequestAttribute("user") user: String,
                         @PathVariable("id") id: String): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            transaction {
                val count = UserRelations.select {
                    (UserRelations.from eq fid) and (UserRelations.to eq tid)
                }.count()

                if (count == 0) status = HttpStatus.NOT_FOUND
            }
        }

        return ResponseEntity(status)
    }

    @Authentication
    @DeleteMapping(
            "/relations/users/{id}"
    )
    fun deleteUserRelation(@RequestAttribute("user") user: String,
                            @PathVariable id: String): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val count = transaction {
                UserRelations.deleteWhere {
                    (UserRelations.from eq fid) and (UserRelations.to eq tid)
                }
            }

            if (count == 0) status = HttpStatus.BAD_REQUEST
        }

        return ResponseEntity(status)
    }
}