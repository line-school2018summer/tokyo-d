package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.model.UserMessages
import com.proelbtn.linesc.model.UserRelations
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
class UserMessagesController {
    @Authentication
    @PostMapping(
            "/messages/users/{id}"
    )
    fun createUserMessage(@RequestBody req: PostMessageRequest,
                          @RequestAttribute("user") user: String,
                          @PathVariable("id") id: String): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST
        if (user == id) status = HttpStatus.BAD_REQUEST
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        val fid = UUID.fromString(user)
        val tid = UUID.fromString(id)
        val rel = transaction { UserRelations.select {
                (UserRelations.from eq fid) and (UserRelations.to eq tid)
            }.firstOrNull()
        }
        if (rel == null) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val now = DateTime.now()

            transaction { UserMessages.insert {
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