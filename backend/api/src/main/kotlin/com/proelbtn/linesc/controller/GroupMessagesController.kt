package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.exceptions.BadRequestException
import com.proelbtn.linesc.exceptions.ForbiddenException
import com.proelbtn.linesc.model.UserGroupMessages
import com.proelbtn.linesc.model.UserGroupRelations
import com.proelbtn.linesc.request.CreateMessageRequest
import com.proelbtn.linesc.response.MessageResponse
import io.swagger.annotations.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.web.bind.annotation.*
import java.util.*

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
                (ApiResponse(code = 403, message = "所属関係がない。"))
           ]
    )
    fun createGroupMessage(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "作成するグループメッセージの情報") @RequestBody req: CreateMessageRequest) {
        val from = req.from
        val to = req.to

        val rel = transaction { UserGroupRelations.select {
                (UserGroupRelations.from eq from) and (UserGroupRelations.to eq to)
            }.firstOrNull()
        }
        if (rel == null) throw ForbiddenException()

        val id = UUID.randomUUID()
        val now = DateTime.now()

        transaction { UserGroupMessages.insert {
                it[UserGroupMessages.id] = id
                it[UserGroupMessages.from] = from
                it[UserGroupMessages.to] = to
                it[UserGroupMessages.content] = req.content
                it[UserGroupMessages.createdAt] = now
            }
        }
    }

    @Authentication
    @GetMapping(
            "/messages/groups/{id}"
    )
    @ApiOperation(
            value = "グループメッセージの取得用",
            notes = "グループメッセージを取得するのに使用するエンドポイント",
            response = MessageResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にメッセージが取得できた。")),
                (ApiResponse(code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun getGroupMessage(
            @ApiParam(value = "取得したいトークのグループのID") @PathVariable("id") id: UUID,
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "このメッセージID以降に送信されたメッセージを取得する", required = false) @RequestParam("since_id", required = false) sinceId: UUID?,
            @ApiParam(value = "このメッセージID以前に送信されたメッセージを取得する", required = false) @RequestParam("max_id", required = false) maxId: UUID?,
            @ApiParam(value = "取得するメッセージの件数（最大100件）", required = false, defaultValue = "20") @RequestParam("count", required = false, defaultValue = "20") count: Int = 20
                ): List<MessageResponse> {
        var response: List<MessageResponse>? = null

        if (count < 0 || count > 100) throw BadRequestException()

        val query: Query? =
                if (sinceId == null && maxId == null) {
                    transaction {
                        UserGroupMessages.select {
                            (UserGroupMessages.to eq id)
                        }.orderBy(UserGroupMessages.createdAt).limit(count)
                    }
                }
                else if (sinceId == null && maxId != null) {
                    transaction {
                        val maxDate = UserGroupMessages.select {
                            (UserGroupMessages.to eq id) and
                                    (UserGroupMessages.id eq maxId)
                        }.firstOrNull()?.get(UserGroupMessages.createdAt)

                        if (maxDate == null) throw ForbiddenException()

                        UserGroupMessages.select {
                            UserGroupMessages.createdAt less maxDate
                        }.orderBy(UserGroupMessages.createdAt).limit(count)
                    }
                }
                else if (sinceId != null && maxId == null) {
                    transaction {
                        val sinceDate = UserGroupMessages.select {
                            (UserGroupMessages.to eq id) and
                                    (UserGroupMessages.id eq sinceId)
                        }.firstOrNull()?.get(UserGroupMessages.createdAt)

                        if (sinceDate == null) throw ForbiddenException()

                        UserGroupMessages.select {
                            UserGroupMessages.createdAt greater sinceDate
                        }.orderBy(UserGroupMessages.createdAt, isAsc = true).limit(count)
                    }
                }
                else throw ForbiddenException()

            response = query?.map { MessageResponse(
                    it[UserGroupMessages.id],
                    it[UserGroupMessages.from],
                    it[UserGroupMessages.to],
                    it[UserGroupMessages.content],
                    it[UserGroupMessages.createdAt].toString()
            ) }

        return response!!
    }
}