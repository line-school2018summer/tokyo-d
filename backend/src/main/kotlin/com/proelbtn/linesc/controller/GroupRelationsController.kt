package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.request.CreateRelationRequest
import com.proelbtn.linesc.message.response.RelationResponse
import com.proelbtn.linesc.model.UserGroupRelations
import com.proelbtn.linesc.model.UserGroups
import com.proelbtn.linesc.validator.validate_id
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
                            @RequestBody req: CreateRelationRequest): ResponseEntity<RelationResponse> {
        var message: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(req.from)
            val tid = UUID.fromString(req.to)
            val now = DateTime.now()

            transaction {
                if (status == HttpStatus.OK) {
                    val query = UserGroupRelations.select {
                        (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
                    }.firstOrNull()

                    if (query != null) status = HttpStatus.BAD_REQUEST
                }

                if (status == HttpStatus.OK) {
                    UserGroupRelations.insert {
                        it[from] = fid
                        it[to] = tid
                        it[createdAt] = now
                    }
                }
            }

            if (status == HttpStatus.OK) {
                message = RelationResponse (req.from, req.to, now.toString())
            }

        }

        return ResponseEntity(status)
    }

    @Authentication
    @GetMapping(
            "/relations/groups/{id}"
    )
    fun getGroupRelation(@RequestAttribute("user") user: String,
                        @PathVariable("id") id: String): ResponseEntity<RelationResponse> {
        var message: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val rel = transaction { UserGroupRelations.select {
                    (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
                }.firstOrNull()
            }

            if (rel == null) status = HttpStatus.NOT_FOUND
            else {
                message = RelationResponse (
                        rel[UserGroupRelations.from].toString(),
                        rel[UserGroupRelations.to].toString(),
                        rel[UserGroupRelations.createdAt].toString()
                )
            }
        }

        return ResponseEntity(message, status)
    }

    @Authentication
    @DeleteMapping(
            "/relations/groups/{id}"
    )
    fun deleteGroupRelation(@RequestAttribute("user") user: String,
                            @PathVariable id: String): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val count = transaction {
                UserGroupRelations.deleteWhere {
                    (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
                }
            }

            if (count == 0) status = HttpStatus.BAD_REQUEST
        }

        return ResponseEntity(status)
    }
}