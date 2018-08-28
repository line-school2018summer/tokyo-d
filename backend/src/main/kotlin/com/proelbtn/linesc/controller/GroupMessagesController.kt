package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.request.MessageSelector
import com.proelbtn.linesc.model.UserGroupMessages
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class GroupMessagesController {
    @Authentication
    @PostMapping(
            "/messages/groups/{id}"
    )
    fun createGroupMessage(@RequestAttribute("user") user: String,
                          @PathVariable("id") id: String,
                          @RequestBody selector: MessageSelector): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(id)

        val message = selector.message

        transaction {
            if (message != null) {
                val now = DateTime.now()
                UserGroupMessages.insert {
                    it[from] = fid
                    it[to] = tid
                    it[content] = message
                    it[createdAt] = now
                }
            }
            else status = HttpStatus.BAD_REQUEST
        }

        return ResponseEntity(status)
    }
}