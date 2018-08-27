package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.request.UserSelector
import com.proelbtn.linesc.message.response.StatusMessage
import com.proelbtn.linesc.model.UserRelations
import com.proelbtn.linesc.model.Users
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

class UserRelationsController {
    @Authentication
    @PostMapping (
            "/relations/users"
    )
    fun createUserRelation(@RequestAttribute("user") user: String,
                           @RequestBody selector: UserSelector): ResponseEntity<Unit> {
        var status = HttpStatus.INTERNAL_SERVER_ERROR

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(selector.id)

        // validation
        // We don't need to check fid.count() because token is issued
        val tq = Users.select { Users.id eq tid }.count()
        if (tq == 0) return ResponseEntity(HttpStatus.BAD_REQUEST)
        else if (tq > 1) return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val query = UserRelations.select {
            (UserRelations.from eq fid) and (UserRelations.to eq tid)
        }.firstOrNull()

        if (query == null) {
            UserRelations.insert {
                it[from] = fid
                it[to] = tid
            }

            val relation = UserRelations.select {
                (UserRelations.from eq fid) and (UserRelations.to eq tid)
            }.firstOrNull()

            if (relation != null) status = HttpStatus.OK
        }
        else status = HttpStatus.BAD_REQUEST

        return ResponseEntity(status)
    }

    @Authentication
    @GetMapping(
            "/relations/users/{id}"
    )
    fun getUserRelation(@RequestAttribute("user") user: String,
                           @RequestBody selector: UserSelector): ResponseEntity<Unit> {
        var status = HttpStatus.INTERNAL_SERVER_ERROR

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(selector.id)

        val query = UserRelations.select {
            (UserRelations.from eq fid) and (UserRelations.to eq tid)
        }.firstOrNull()

        if (query != null) status = HttpStatus.FOUND
        else status = HttpStatus.NOT_FOUND

        return ResponseEntity(status)
    }

    @Authentication
    @DeleteMapping(
            "/relations/users/{id}"
    )
    fun deleteUserRelation(@RequestAttribute("user") user: String,
                        @RequestBody selector: UserSelector): ResponseEntity<Unit> {
        var status = HttpStatus.INTERNAL_SERVER_ERROR

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(selector.id)

        val count = UserRelations.deleteWhere {
            (UserRelations.from eq fid) and (UserRelations.to eq tid)
        }

        if (count == 1) status = HttpStatus.OK

        return ResponseEntity(status)
    }
}