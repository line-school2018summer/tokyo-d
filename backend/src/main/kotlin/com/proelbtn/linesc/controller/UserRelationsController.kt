package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.request.CreateRelationRequest
import com.proelbtn.linesc.response.RelationResponse
import com.proelbtn.linesc.model.UserRelations
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
                            @RequestBody req: CreateRelationRequest): ResponseEntity<RelationResponse> {
        var res: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST
        if (user == req.from) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(req.from)
            val tid = UUID.fromString(req.to)
            val now = DateTime.now()

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
                        it[createdAt] = now
                    }
                }
            }

            if (status == HttpStatus.OK) {
                res = RelationResponse(req.from, req.to, now.toString())
            }

        }

        return ResponseEntity(res, status)
    }

    @Authentication
    @GetMapping(
            "/relations/users/{id}"
    )
    fun getUserRelation(@RequestAttribute("user") user: String,
                         @PathVariable("id") id: String): ResponseEntity<RelationResponse> {
        var res: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val rel = transaction { UserRelations.select {
                    (UserRelations.from eq fid) and (UserRelations.to eq tid)
                }.firstOrNull()
            }

            if (rel == null) status = HttpStatus.NOT_FOUND
            else {
                res = RelationResponse(
                        rel[UserRelations.from].toString(),
                        rel[UserRelations.to].toString(),
                        rel[UserRelations.createdAt].toString()
                )
            }
        }

        return ResponseEntity(res, status)
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