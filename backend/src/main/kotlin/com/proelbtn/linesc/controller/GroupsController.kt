package com.proelbtn.linesc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proelbtn.linesc.model.UserGroups
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GroupsController {
    @GetMapping(
            "/groups/id/{id}"
    )
    fun getGroupInformationFromId(@PathVariable("id") id: String): ResponseEntity<String> {
        var message: String = ""
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val query = UserGroups.select { UserGroups.id eq UUID.fromString(id) }

            if (query.count() == 1) {
                val user = query.first()
                val mapper = ObjectMapper()
                val node = mapper.createObjectNode()
                node.put("id", user[UserGroups.id].toString())
                node.put("sid", user[UserGroups.sid].toString())
                node.put("name", user[UserGroups.name].toString())
                node.put("owner", user[UserGroups.owner].toString())
                node.put("created_at", user[UserGroups.createdAt].toString())
                node.put("updated_at", user[UserGroups.updatedAt].toString())

                message = mapper.writeValueAsString(node)
                status = HttpStatus.OK
            }
            else {
                message = "{\"status\": \"Error\"}"
                status = HttpStatus.NOT_FOUND
            }
        }

        return ResponseEntity(message, status)
    }

    @DeleteMapping(
            "/groups/id/{id}"
    )
    fun deleteGroupInformationFromId(@PathVariable("id") id: String): ResponseEntity<String> {
        var message: String = ""
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val count = UserGroups.deleteWhere { UserGroups.id eq UUID.fromString(id) }

            if (count == 1) {
                message = "{\"status\": \"Ok\"}"
                status = HttpStatus.OK
            }
            else if (count == 0){
                message = "{\"status\": \"Error\"}"
                status = HttpStatus.BAD_REQUEST
            }
            else {
                message = "{\"status\": \"Error\"}"
                status = HttpStatus.INTERNAL_SERVER_ERROR
            }
        }

        return ResponseEntity(message, status)
    }
}