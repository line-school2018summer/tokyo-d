package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.exceptions.BadRequestException
import com.proelbtn.linesc.exceptions.ForbiddenException
import com.proelbtn.linesc.model.UserMessages
import com.proelbtn.linesc.model.UserRelations
import com.proelbtn.linesc.request.CreateMessageRequest
import com.proelbtn.linesc.response.MessageResponse
import com.proelbtn.linesc.validator.validate_id
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.websocket.server.PathParam

@RestController
class UserMessagesController {
    @Authentication
    @PostMapping(
            value = "/messages/users",
            produces = [ APPLICATION_JSON_VALUE ]
    )
    @ApiOperation(
            value = "ユーザメッセージの投稿用",
            notes = "ユーザメッセージを投稿するのに使用するエンドポイント",
            response = Unit::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にユーザメッセージを投稿できた。")),
                (ApiResponse(code = 403, message = "友達関係がない。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun createUserMessage(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "作成するユーザメッセージの情報") @RequestBody req: CreateMessageRequest
                ) {
        val from = req.from
        val to = req.to

        val rel = transaction { UserRelations.select {
                (UserRelations.from eq from) and (UserRelations.to eq to)
            }.firstOrNull()
        }

        if (rel == null) throw ForbiddenException()

        val id = UUID.randomUUID()
        val now = DateTime.now()

        transaction { UserMessages.insert {
                it[UserMessages.id] = id
                it[UserMessages.from] = from
                it[UserMessages.to] = to
                it[UserMessages.content] = req.content
                it[UserMessages.createdAt] = now
            }
        }
    }

    @Authentication
    @GetMapping(
            "/messages/users/{id}"
    )
    @ApiOperation(
            value = "ユーザメッセージの取得用",
            notes = "ユーザメッセージを取得するのに使用するエンドポイント",
            response = MessageResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にユーザメッセージを取得できた。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getGroupMessage(
            @ApiParam(value = "取得したいトークのユーザのID") @PathVariable("id") id: UUID,
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "このメッセージID以降に送信されたメッセージを取得する", required = false) @RequestParam("since_id", required = false) sinceId: UUID?,
            @ApiParam(value = "このメッセージID以前に送信されたメッセージを取得する", required = false) @RequestParam("max_id", required = false) maxId: UUID?,
            @ApiParam(value = "取得するメッセージの件数（最大100件）", required = false, defaultValue = "20") @RequestParam("count", required = false, defaultValue = "20") count: Int = 20
    ): List<MessageResponse> {
        var response: List<MessageResponse>? = null

        if (count < 0 || count > 100) throw BadRequestException()

        val query =
                if (sinceId == null && maxId == null) {
                    transaction {
                        UserMessages.selectAll()
                                .orderBy(UserMessages.createdAt).limit(count).toList()
                    }
                }
                else if (sinceId == null && maxId != null) {
                    transaction {
                        val maxDate = UserMessages.select {
                            UserMessages.id eq maxId
                        }.firstOrNull()?.get(UserMessages.createdAt)

                        if (maxDate == null) throw ForbiddenException()

                        UserMessages.select {
                            UserMessages.createdAt less maxDate
                        }.orderBy(UserMessages.createdAt).limit(count).toList()
                    }
                }
                else if (sinceId != null && maxId == null) {
                    transaction {
                        val sinceDate = UserMessages.select {
                            UserMessages.id eq sinceId
                        }.firstOrNull()?.get(UserMessages.createdAt)

                        if (sinceDate == null) throw ForbiddenException()

                        UserMessages.select {
                            UserMessages.createdAt greater sinceDate
                        }.orderBy(UserMessages.createdAt, isAsc = true).limit(count).toList()
                    }
                }
                else throw ForbiddenException()

        response = query.map { MessageResponse(
                it[UserMessages.id],
                it[UserMessages.from],
                it[UserMessages.to],
                it[UserMessages.content],
                it[UserMessages.createdAt].toString()
        ) }

        return response
    }
}