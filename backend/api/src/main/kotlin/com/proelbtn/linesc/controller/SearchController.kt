package com.proelbtn.linesc.controller

import com.proelbtn.linesc.exceptions.NotFoundException
import com.proelbtn.linesc.response.GroupResponse
import com.proelbtn.linesc.response.UserResponse
import com.proelbtn.linesc.model.UserGroups
import com.proelbtn.linesc.model.Users
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController {
    @GetMapping(
            value = "/search/users/{sid}",
            produces = [ APPLICATION_JSON_VALUE ]
    )
    @ApiOperation(
            value = "ユーザの検索用",
            notes = "ユーザを検索するために使用するエンドポイント。",
            response = UserResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にユーザを検索できた。" ))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun searchUserInformation(
            @ApiParam(value = "ユーザのID", required = true) @PathVariable("sid") sid: String
                ): UserResponse {
        var message: UserResponse? = null

        transaction {
            val query = Users.select { Users.sid eq sid }

            if (query.count() == 0) throw NotFoundException()

            val user = query.first()
            message = UserResponse(
                    user[Users.id],
                    user[Users.sid],
                    user[Users.name],
                    user[Users.createdAt].toString(),
                    user[Users.updatedAt].toString()
            )
        }

        return message!!
    }

    @GetMapping(
            value = "/search/groups/{sid}",
            produces = [ APPLICATION_JSON_VALUE ]
    )
    @ApiOperation(
            value = "グループの検索用",
            notes = "グループを検索するために使用するエンドポイント。",
            response = GroupResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にグループを検索できた。")),
                (ApiResponse(code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun searchGroupInformation(
            @ApiParam(value = "グループのID", required = true) @PathVariable("sid") sid: String
                ): GroupResponse {
        var message: GroupResponse? = null

        transaction {
            val query = UserGroups.select { UserGroups.sid eq sid }

            if (query.count() == 0) throw NotFoundException()

            var group = query.first()

            message = GroupResponse(
                    group[UserGroups.id],
                    group[UserGroups.sid],
                    group[UserGroups.name],
                    group[UserGroups.owner],
                    group[UserGroups.createdAt].toString(),
                    group[UserGroups.updatedAt].toString()
            )
        }

        return message!!
    }
}
