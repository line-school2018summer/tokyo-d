package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.exceptions.ForbiddenException
import com.proelbtn.linesc.exceptions.NotFoundException
import com.proelbtn.linesc.request.CreateGroupRequest
import com.proelbtn.linesc.response.GroupResponse
import com.proelbtn.linesc.model.UserGroups
import io.swagger.annotations.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class GroupsController {
    @Authentication
    @PostMapping(
            "/groups"
    )
    @ApiOperation(
            value = "グループの作成用",
            notes = "グループを作成するのに使用するエンドポイント",
            response = GroupResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にグループを作成できた。")),
                (ApiResponse( code = 403, message = "既にグループが存在する。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun createGroupsInformation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "作成するグループの情報") @RequestBody req: CreateGroupRequest
                ): GroupResponse {
        val id = UUID.randomUUID()
        val sid = req.sid
        val name = req.name
        val owner = UUID.fromString(user)
        val now = DateTime.now()

        transaction {
            val group = UserGroups.select { UserGroups.sid eq sid }.firstOrNull()

            if (group != null) throw ForbiddenException()

            UserGroups.insert {
                it[UserGroups.id] = id
                it[UserGroups.sid] = sid
                it[UserGroups.name] = name
                it[UserGroups.owner] = owner
                it[UserGroups.createdAt] = now
                it[UserGroups.updatedAt] = now
            }
        }

        return GroupResponse(id, sid, name, owner, now.toString(), now.toString())
    }

    @GetMapping(
            value = "/groups/{id}",
            produces = [ APPLICATION_JSON_VALUE ]
    )
    @ApiOperation(
            value = "グループの取得用",
            notes = "グループを取得するのに使用するエンドポイント",
            response = GroupResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にグループを取得できた。" )),
                (ApiResponse(code = 404, message = "取得するグループが存在しなかった。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getGroupInformation(
            @ApiParam(value = "グループのID") @PathVariable("id") id: UUID
                ): GroupResponse {
        // operation
        val group = transaction { UserGroups.select { UserGroups.id eq id }.firstOrNull() }

        if (group == null) throw NotFoundException()

        return GroupResponse(
                    group[UserGroups.id],
                    group[UserGroups.sid],
                    group[UserGroups.name],
                    group[UserGroups.owner],
                    group[UserGroups.createdAt].toString(),
                    group[UserGroups.updatedAt].toString()
            )
    }

    @Authentication
    @DeleteMapping(
            value = "/groups/{id}"
    )
    @ApiOperation(
            value = "グループの削除用",
            notes = "グループを削除するのに使用するエンドポイント"
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にグループを削除できた。" )),
                (ApiResponse( code = 403, message = "権限がないか削除するべきグループが存在しない。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun deleteGroupInformation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "グループのID") @PathVariable("id") id: UUID
                ) {
        val count = transaction {
            UserGroups.deleteWhere { (UserGroups.id eq id) and (UserGroups.owner eq user) }
        }

        if (count == 0) throw ForbiddenException()
    }
}