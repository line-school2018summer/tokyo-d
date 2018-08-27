package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.message.request.GroupSelector
import com.proelbtn.linesc.message.response.GroupResponseMessage
import com.proelbtn.linesc.model.UserGroups
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
                                @RequestBody selector: GroupSelector): ResponseEntity<GroupResponseMessage> {
        var message: GroupResponseMessage? = null
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        val rsid = selector.sid
        val rname = selector.name


        if (rsid == null || rname == null) status = HttpStatus.BAD_REQUEST
        else if (rsid.isEmpty() || rname.isEmpty()) status = HttpStatus.BAD_REQUEST
        else {
            transaction {
                val query = UserGroups.select { UserGroups.sid eq rsid }.firstOrNull()

                if (query != null) status = HttpStatus.BAD_REQUEST
                else {
                    val now = DateTime.now()
                    val uuid = UUID.randomUUID()

                    UserGroups.insert {
                        it[id] = uuid
                        it[sid] = rsid
                        it[name] = rname
                        it[owner] = UUID.fromString(user)
                        it[createdAt] = now
                        it[updatedAt] = now
                    }

                    val user = UserGroups.select { UserGroups.id eq uuid }.firstOrNull()

                    if (user != null) {
                        message = GroupResponseMessage(
                                user[UserGroups.id].toString(),
                                user[UserGroups.sid].toString(),
                                user[UserGroups.name].toString(),
                                user[UserGroups.owner].toString(),
                                user[UserGroups.createdAt].toString(),
                                user[UserGroups.updatedAt].toString()
                        )
                        status = HttpStatus.OK
                    }
                }
            }
        }

        return ResponseEntity(message, status)
    }

    @GetMapping(
            "/groups/{id}"
    )
    fun getGroupInformationFromId(@PathVariable("id") id: String): ResponseEntity<GroupResponseMessage> {
        var message: GroupResponseMessage? = null
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val group = UserGroups.select { UserGroups.id eq UUID.fromString(id) }.firstOrNull()

            if (group != null) {
                message = GroupResponseMessage(
                        group[UserGroups.id].toString(),
                        group[UserGroups.sid].toString(),
                        group[UserGroups.name].toString(),
                        group[UserGroups.owner].toString(),
                        group[UserGroups.createdAt].toString(),
                        group[UserGroups.updatedAt].toString()
                )
                status = HttpStatus.OK
            }
            else status = HttpStatus.NOT_FOUND
        }

        return ResponseEntity(message, status)
    }

    @DeleteMapping(
            "/groups/{id}"
    )
    fun deleteGroupInformationFromId(@PathVariable("id") id: String): ResponseEntity<Unit> {
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val count = UserGroups.deleteWhere { UserGroups.id eq UUID.fromString(id) }

            if (count == 1) status = HttpStatus.OK
            else if (count == 0) status = HttpStatus.BAD_REQUEST
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(status)
    }
}