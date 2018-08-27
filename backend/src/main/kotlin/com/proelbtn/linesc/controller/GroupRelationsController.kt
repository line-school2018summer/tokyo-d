package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.request.GroupSelector
import com.proelbtn.linesc.model.UserGroupRelations
import com.proelbtn.linesc.model.UserGroups
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class GroupRelationsController {
    @Authentication
    @PostMapping(
            "/relations/groups"
    )
    fun createGroupRelation(@RequestAttribute("user") user: String,
                            @RequestBody selector: GroupSelector): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(selector.id)

        transaction {
            // validation
            // We don't need to check fid.count() because token is issued
            val tq = UserGroups.select { UserGroups.id eq tid }.count()
            if (tq == 0) status = HttpStatus.BAD_REQUEST
            else if (tq > 1) status = HttpStatus.INTERNAL_SERVER_ERROR

            if (status == HttpStatus.OK) {
                val query = UserGroupRelations.select {
                    (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
                }.firstOrNull()

                if (query == null) {
                    val now = DateTime.now()
                    UserGroupRelations.insert {
                        it[from] = fid
                        it[to] = tid
                        it[createdAt] = now
                    }

                    val relation = UserGroupRelations.select {
                        (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
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
            "/relations/groups/{id}"
    )
    fun getGroupRelation(@RequestAttribute("user") user: String,
                        @PathVariable("id") id: String): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(id)

        transaction {
            val count = UserGroupRelations.select {
                (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
            }.count()

            if (count == 1) status = HttpStatus.FOUND
            else if (count == 0) status = HttpStatus.NOT_FOUND
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(status)
    }

    @Authentication
    @DeleteMapping(
            "/relations/groups/{id}"
    )
    fun deleteGroupRelation(@RequestAttribute("user") user: String,
                           @PathVariable id: String): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(id)

        transaction {
            val count = UserGroupRelations.deleteWhere {
                (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
            }

            if (count == 0) status = HttpStatus.BAD_REQUEST
            else if (count > 1) status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(status)
    }
}