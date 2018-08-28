package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.model.UserGroupMessages
import com.proelbtn.linesc.model.UserGroupRelations
import com.proelbtn.linesc.request.PostMessageRequest
import com.proelbtn.linesc.validator.validate_id
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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
                          @RequestBody req: PostMessageRequest): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(id)
        val rel = transaction { UserGroupRelations.select {
                (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
            }.firstOrNull()
        }
        if (rel == null) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val now = DateTime.now()

            transaction { UserGroupMessages.insert {
                    it[from] = fid
                    it[to] = tid
                    it[content] = req.content
                    it[createdAt] = now
                }
            }
        }

        return ResponseEntity(status)
    }
}