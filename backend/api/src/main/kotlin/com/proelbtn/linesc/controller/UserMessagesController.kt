package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.model.UserMessages
import com.proelbtn.linesc.model.UserRelations
import com.proelbtn.linesc.request.CreateMessageRequest
import com.proelbtn.linesc.response.MessageResponse
import com.proelbtn.linesc.validator.validate_id
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.websocket.server.PathParam

@RestController
class UserMessagesController {
    @Authentication
    @PostMapping(
            "/messages/users"
    )
    fun createUserMessage(@RequestAttribute("user") user: String,
                          @RequestBody req: CreateMessageRequest): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        val fid = UUID.fromString(req.from)
        val tid = UUID.fromString(req.to)

        val rel = transaction { UserRelations.select {
                (UserRelations.from eq fid) and (UserRelations.to eq tid)
            }.firstOrNull()
        }
        if (rel == null) status = HttpStatus.BAD_REQUEST

        // operation
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

    @Authentication
    @GetMapping(
            "/messages/users"
    )
    fun getUserMessage(@RequestAttribute("user") user: UUID,
                       @RequestParam("f") from: UUID,
                       @RequestParam("t") to: UUID,
                       @RequestParam("a", required = false) after: String?): ResponseEntity<List<MessageResponse>> {
        var status: HttpStatus = HttpStatus.OK

        // operation
        var messages = transaction { UserMessages.select {
                (UserMessages.from eq from) and (UserMessages.to eq to)
            }.orderBy(Pair(UserMessages.createdAt, SortOrder.DESC)).toList()
        }

        // object mapping
        var msg = messages.map {
            MessageResponse (
                it[UserMessages.from],
                it[UserMessages.to],
                it[UserMessages.content]
            )
        }

        return ResponseEntity(msg, status)
    }
}