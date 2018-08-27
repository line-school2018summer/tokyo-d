package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.request.UserSelector
import com.proelbtn.linesc.model.UserRelations
import com.proelbtn.linesc.model.Users
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
    @PostMapping (
            "/relations/users"
    )
    fun createUserRelation(@RequestAttribute("user") user: String,
                           @RequestBody selector: UserSelector): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(selector.id)

        transaction {
            // validation
            // We don't need to check fid.count() because token is issued

            val tq = Users.select { Users.id eq tid }.count()
            if (tq == 0) status = HttpStatus.BAD_REQUEST
            else if (tq > 1) status = HttpStatus.INTERNAL_SERVER_ERROR

            if (status == HttpStatus.OK) {
                val query = UserRelations.select {
                    (UserRelations.from eq fid) and (UserRelations.to eq tid)
                }.firstOrNull()

                if (query == null) {
                    val now = DateTime.now()
                    UserRelations.insert {
                        it[from] = fid
                        it[to] = tid
                        it[createdAt] = now
                    }

                    val relation = UserRelations.select {
                        (UserRelations.from eq fid) and (UserRelations.to eq tid)
                    }.firstOrNull()

                    if (relation == null) status = HttpStatus.INTERNAL_SERVER_ERROR
                }
                else status = HttpStatus.BAD_REQUEST
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

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(id)

        transaction {
            val count = UserRelations.select {
                (UserRelations.from eq fid) and (UserRelations.to eq tid)
            }.count()

            if (count == 1) status = HttpStatus.FOUND
            else if (count == 0) status = HttpStatus.NOT_FOUND
            else status = HttpStatus.INTERNAL_SERVER_ERROR
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

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(id)

        transaction {
            val count = UserRelations.deleteWhere {
                (UserRelations.from eq fid) and (UserRelations.to eq tid)
            }

            if (count == 0) status = HttpStatus.BAD_REQUEST
            else if (count > 1) status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(status)
    }
}