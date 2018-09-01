package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.request.CreateGroupRequest
import com.proelbtn.linesc.response.GroupResponse
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
class GroupsController {
    @Authentication
    @PostMapping(
            "/groups"
    )
    fun createGroupsInformation(@RequestAttribute("user") user: String,
                                @RequestBody req: CreateGroupRequest): ResponseEntity<GroupResponse> {
        var res: GroupResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        // operation
        if (status == HttpStatus.OK) {
            val id = UUID.randomUUID()
            val sid = req.sid!!
            val name = req.name!!
            val owner = UUID.fromString(user)
            val now = DateTime.now()

            transaction {
                val group = UserGroups.select { UserGroups.sid eq sid }.firstOrNull()

                if (group != null) status = HttpStatus.BAD_REQUEST
                else {
                    UserGroups.insert {
                        it[UserGroups.id] = id
                        it[UserGroups.sid] = sid
                        it[UserGroups.name] = name
                        it[UserGroups.owner] = owner
                        it[UserGroups.createdAt] = now
                        it[UserGroups.updatedAt] = now
                    }
                }
            }

            if (status == HttpStatus.OK)
                res = GroupResponse(id, sid, name, owner, now.toString(), now.toString())
        }

        return ResponseEntity(res, status)
    }

    @GetMapping(
            "/groups/{id}"
    )
    fun getGroupInformation(@PathVariable("id") id: String): ResponseEntity<GroupResponse> {
        var message: GroupResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(id)) status = HttpStatus.BAD_REQUEST

        // operation
        if (status == HttpStatus.OK) {
            val group = transaction { UserGroups.select { UserGroups.id eq UUID.fromString(id) }.firstOrNull() }

            if (group == null) status = HttpStatus.NOT_FOUND
            else {
                message = GroupResponse(
                        group[UserGroups.id],
                        group[UserGroups.sid],
                        group[UserGroups.name],
                        group[UserGroups.owner],
                        group[UserGroups.createdAt].toString(),
                        group[UserGroups.updatedAt].toString()
                )
            }
        }

        return ResponseEntity(message, status)
    }

    @Authentication
    @DeleteMapping(
            "/groups/{id}"
    )
    fun deleteGroupInformation(@RequestAttribute("user") user: String,
                                     @PathVariable("id") id: String): ResponseEntity<Unit> {
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user) && !validate_id(id)) status = HttpStatus.BAD_REQUEST

        val count = transaction {
            UserGroups.deleteWhere { (UserGroups.id eq UUID.fromString(id)) and (UserGroups.owner eq UUID.fromString(user)) }
        }

        if (count == 0) status = HttpStatus.BAD_REQUEST

        return ResponseEntity(status)
    }
}