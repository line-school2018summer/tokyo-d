package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.model.UserGroupMessages
import com.proelbtn.linesc.model.UserGroupRelations
import com.proelbtn.linesc.model.UserMessages
import com.proelbtn.linesc.request.CreateMessageRequest
import com.proelbtn.linesc.response.MessageResponse
import com.proelbtn.linesc.validator.validate_id
import io.swagger.annotations.*
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
class GroupMessagesController {
    @Authentication
    @PostMapping(
            "/messages/groups"
    )
    @ApiOperation(
            value = "グループメッセージの投稿用",
            notes = "グループメッセージを投稿するのに使用するエンドポイント",
            response = Unit::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にグループメッセージが投稿できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
           ]
    )
    fun createGroupMessage(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "作成するグループメッセージの情報") @RequestBody req: CreateMessageRequest): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        val fid = UUID.fromString(req.from)
        val tid = UUID.fromString(req.to)
        val rel = transaction { UserGroupRelations.select {
                (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
            }.firstOrNull()
        }
        if (rel == null) status = HttpStatus.BAD_REQUEST

        // operation
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

    @Authentication
    @GetMapping(
            "/messages/groups/"
    )
    @ApiOperation(
            value = "グループメッセージの取得用",
            notes = "グループメッセージを取得するのに使用するエンドポイント",
            response = Unit::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にメッセージが取得できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun getGroupMessage(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "送信先のグループのID") @RequestParam("t") to: UUID,
            @ApiParam(value = "この時間以降に作成されたグループメッセージを取得する。") @RequestParam("a", required = false) after: String?
                ): ResponseEntity<List<MessageResponse>> {
        var status: HttpStatus = HttpStatus.OK

        // operation
        var messages = transaction { UserGroupMessages.select {
                UserGroupMessages.to eq to
            }.orderBy(Pair(UserGroupMessages.createdAt, SortOrder.DESC)).toList()
        }

        // object mapping
        var msg = messages.map {
            MessageResponse (
                    it[UserGroupMessages.from],
                    it[UserGroupMessages.to],
                    it[UserGroupMessages.content]
            )
        }

        return ResponseEntity(msg, status)
    }
}